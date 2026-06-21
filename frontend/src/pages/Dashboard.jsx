import { useEffect, useState, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
import { ArrowLeft, Sparkles, History, RefreshCw, User as UserIcon } from 'lucide-react';
import { toast } from 'react-toastify';
import api from '../services/api';
import BookCard from '../components/BookCard';

const Dashboard = () => {
    // Đã dùng biến user để hiển thị tên ở Tiêu đề (Sửa lỗi 1)
    const { user } = useContext(AuthContext); 
    const navigate = useNavigate();

    const [recommendations, setRecommendations] = useState([]);
    const [histories, setHistory] = useState([]);
    const [loading, setLoading] = useState(true);
    const [generating, setGenerating] = useState(false);
    
    // Biến cò mồi để kích hoạt lại useEffect khi cần thiết
    const [refreshKey, setRefreshKey] = useState(0); 

    // Đưa hàm fetch vào trong useEffect để tránh lỗi "set-state-in-effect" (Sửa lỗi 2)
    useEffect(() => {
        let isMounted = true;

        const fetchData = async () => {
            try {
                const [recRes, histRes] = await Promise.all([
                    api.get('/recommendations'),
                    api.get('/history')
                ]);
                if (isMounted) {
                    setRecommendations(recRes.data);
                    setHistory(histRes.data);
                }
            } catch (err) {
                console.error("Lỗi:", err);
                toast.error("Không thể tải dữ liệu cá nhân!");
            } finally {
                if (isMounted) setLoading(false);
            }
        };

        fetchData();

        return () => {
            isMounted = false;
        };
    }, [refreshKey]); // Chạy lại mỗi khi refreshKey bị thay đổi

    const handleGenerateAI = async () => {
        setGenerating(true);
        try {
            await api.post('/recommendations/generate');
            toast.success("✨ Thuật toán AI đã phân tích xong gu của bro!");
            // Đổi giá trị cò mồi để useEffect tự động kéo data mới
            setRefreshKey(prev => prev + 1); 
        } catch { // Đã xóa biến error thừa (Sửa lỗi 3)
            toast.error("Có lỗi khi khởi động lõi AI!");
        } finally {
            setGenerating(false);
        }
    };

    if (loading) return <div className="min-h-screen flex items-center justify-center"><div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div></div>;

    return (
        <div className="min-h-screen bg-gray-50 p-6 md:p-10">
            <div className="max-w-7xl mx-auto">
                
                {/* Header & Nút Back */}
                <div className="flex flex-col md:flex-row justify-between items-start md:items-center mb-8 gap-4">
                    <div>
                        <button onClick={() => navigate('/')} className="flex items-center gap-2 text-gray-600 hover:text-blue-600 mb-2 transition-colors">
                            <ArrowLeft className="w-5 h-5" /> Về trang chủ
                        </button>
                        <h1 className="text-3xl font-bold text-gray-900 flex items-center gap-3">
                            <UserIcon className="w-8 h-8 text-blue-600" />
                            Hồ Sơ Của {user?.fullName} {/* Hiển thị tên user cho xịn */}
                        </h1>
                    </div>

                    <button 
                        onClick={handleGenerateAI}
                        disabled={generating}
                        className="bg-gradient-to-r from-indigo-600 to-purple-600 hover:from-indigo-700 hover:to-purple-700 text-white px-6 py-3 rounded-xl font-bold shadow-md hover:shadow-lg transition-all flex items-center gap-2 disabled:opacity-70"
                    >
                        {generating ? <RefreshCw className="w-5 h-5 animate-spin" /> : <Sparkles className="w-5 h-5" />}
                        {generating ? "Hệ thống đang phân tích..." : "Kích hoạt AI Gợi ý"}
                    </button>
                </div>

                {/* KHU VỰC 1: SÁCH AI GỢI Ý */}
                <div className="mb-12">
                    <h2 className="text-2xl font-bold text-gray-800 mb-6 flex items-center gap-2 border-b pb-2">
                        <Sparkles className="w-6 h-6 text-purple-600" /> 
                        Dành Riêng Cho Bro (AI Tuyển Chọn)
                    </h2>
                    
                    {recommendations.length > 0 ? (
                        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
                            {recommendations.map((rec) => (
                                <div key={rec.id} className="relative">
                                    <BookCard book={rec.book} />
                                    <div className="absolute top-2 right-2 bg-purple-600 text-white text-xs font-bold px-2 py-1 rounded-md shadow-sm z-10">
                                        Điểm AI: {rec.score}
                                    </div>
                                </div>
                            ))}
                        </div>
                    ) : (
                        <div className="bg-white rounded-xl border border-dashed border-gray-300 p-10 text-center">
                            <p className="text-gray-500">Bro chưa có gợi ý nào. Hãy bấm nút "Kích hoạt AI Gợi ý" ở góc trên nhé!</p>
                        </div>
                    )}
                </div>

                {/* KHU VỰC 2: LỊCH SỬ ĐỌC SÁCH */}
                <div>
                    <h2 className="text-2xl font-bold text-gray-800 mb-6 flex items-center gap-2 border-b pb-2">
                        <History className="w-6 h-6 text-blue-600" /> 
                        Lịch Sử Đọc Sách
                    </h2>
                    
                    {histories.length > 0 ? (
                        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
                            {histories.map((hist) => (
                                <div key={hist.id} className="relative">
                                    <BookCard book={hist.book} />
                                    <div className="absolute bottom-20 right-2 bg-blue-100 text-blue-800 text-xs font-bold px-2 py-1 rounded-md z-10 border border-blue-200">
                                        Đang đọc: Trang {hist.lastReadPage}
                                    </div>
                                </div>
                            ))}
                        </div>
                    ) : (
                        <div className="bg-white rounded-xl border border-dashed border-gray-300 p-10 text-center">
                            <p className="text-gray-500">Bro chưa lưu lịch sử đọc cuốn sách nào cả.</p>
                        </div>
                    )}
                </div>

            </div>
        </div>
    );
};

export default Dashboard;