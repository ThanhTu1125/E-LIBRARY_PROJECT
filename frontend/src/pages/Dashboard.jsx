import { useEffect, useState, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
import { ArrowLeft, Sparkles, History, RefreshCw, User as UserIcon, AlertCircle } from 'lucide-react';
import { toast } from 'react-toastify';
import api from '../services/api';
import BookCard from '../components/BookCard';

const Dashboard = () => {
    const { user } = useContext(AuthContext); 
    const navigate = useNavigate();

    const [recommendations, setRecommendations] = useState([]);
    const [histories, setHistory] = useState([]);
    const [fines, setFines] = useState([]); // Khai báo state quản lý tiền phạt
    const [loading, setLoading] = useState(true);
    const [generating, setGenerating] = useState(false);
    const [refreshKey, setRefreshKey] = useState(0); 

    useEffect(() => {
        let isMounted = true;
        const fetchData = async () => {
            if (!user) return; // Đợi Context nạp xong user
            try {
                // Gọi 3 API cùng lúc
                const [recRes, histRes, fineRes] = await Promise.all([
                    api.get('/recommendations'),
                    api.get('/history'),
                    api.get(`/fines/user/${user.id}`)
                ]);
                if (isMounted) {
                    setRecommendations(recRes.data);
                    setHistory(histRes.data);
                    setFines(fineRes.data);
                }
            } catch (err) {
                console.error("Lỗi:", err);
                toast.error("Không thể tải dữ liệu cá nhân!");
            } finally {
                if (isMounted) setLoading(false);
            }
        };
        fetchData();
        return () => { isMounted = false; };
    }, [refreshKey, user]);

    const handleGenerateAI = async () => {
        setGenerating(true);
        try {
            await api.post('/recommendations/generate');
            toast.success("✨ Lõi AI đã tính toán xong!");
            setRefreshKey(prev => prev + 1); 
        } catch {
            toast.error("Khởi động lõi AI thất bại!");
        } finally {
            setGenerating(false);
        }
    };

    // Hàm nộp phạt
    const handlePayFine = async (fineId) => {
        try {
            await api.put(`/fines/${fineId}/pay`);
            toast.success("Thanh toán thành công! Giao dịch minh bạch trên Blockchain.");
            setRefreshKey(prev => prev + 1); // Cập nhật lại UI
        } catch (error) {
            toast.error(error.response?.data || "Lỗi thanh toán!");
        }
    };

    if (loading) return <div className="min-h-screen flex items-center justify-center"><div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div></div>;

    return (
        <div className="min-h-screen bg-gray-50 p-6 md:p-10">
            <div className="max-w-7xl mx-auto">
                <div className="flex flex-col md:flex-row justify-between items-start md:items-center mb-10 gap-4 bg-white p-6 rounded-2xl shadow-sm border border-gray-100">
                    <div>
                        <button onClick={() => navigate('/')} className="flex items-center gap-2 text-gray-500 hover:text-blue-600 mb-2 font-medium">
                            <ArrowLeft className="w-4 h-4" /> Về trang chủ
                        </button>
                        <h1 className="text-3xl font-bold text-gray-900 flex items-center gap-3">
                            <div className="p-3 bg-blue-100 rounded-full"><UserIcon className="w-8 h-8 text-blue-600" /></div>
                            Hồ Sơ Của {user?.fullName}
                        </h1>
                    </div>
                    <button 
                        onClick={handleGenerateAI} disabled={generating}
                        className="bg-gradient-to-r from-purple-600 to-indigo-600 hover:from-purple-700 hover:to-indigo-700 text-white px-6 py-3 rounded-xl font-bold shadow-md flex items-center gap-2 disabled:opacity-70"
                    >
                        {generating ? <RefreshCw className="w-5 h-5 animate-spin" /> : <Sparkles className="w-5 h-5" />}
                        {generating ? "Đang chạy mạng Nơ-ron..." : "Kích hoạt AI Gợi ý"}
                    </button>
                </div>

                {/* KHU VỰC 1: SÁCH AI GỢI Ý */}
                <div className="mb-12">
                    <h2 className="text-2xl font-bold text-gray-800 mb-6 flex items-center gap-2"><Sparkles className="w-6 h-6 text-purple-600" /> Phân Tích Hành Vi & Đề Xuất</h2>
                    {recommendations.length > 0 ? (
                        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-6">
                            {recommendations.map((rec) => (
                                <div key={rec.id} className="relative">
                                    <BookCard book={rec.book} />
                                    <div className="absolute top-2 right-2 bg-purple-600 text-white text-xs font-bold px-2 py-1 rounded-md shadow-md z-10">Độ phù hợp: {rec.score}đ</div>
                                </div>
                            ))}
                        </div>
                    ) : <div className="bg-white rounded-xl border-dashed border-2 border-gray-300 p-10 text-center text-gray-500">Bấm nút "Kích hoạt AI" phía trên để hệ thống học thói quen của bro nhé!</div>}
                </div>

                <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                    {/* KHU VỰC 2: LỊCH SỬ ĐỌC (Chiếm 2 cột) */}
                    <div className="lg:col-span-2">
                        <h2 className="text-2xl font-bold text-gray-800 mb-6 flex items-center gap-2"><History className="w-6 h-6 text-blue-600" /> Tủ Sách Của Tôi</h2>
                        {histories.length > 0 ? (
                            <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
                                {histories.map((hist) => (
                                    <div key={hist.id} className="relative">
                                        <BookCard book={hist.book} />
                                        <div className="absolute top-2 left-2 bg-green-500 text-white text-xs font-bold px-2 py-1 rounded-md shadow-sm z-10">Trang {hist.lastReadPage}</div>
                                    </div>
                                ))}
                            </div>
                        ) : <div className="bg-white rounded-xl border-dashed border-2 p-10 text-center text-gray-500">Chưa cày cuốn nào.</div>}
                    </div>

                    {/* KHU VỰC 3: QUẢN LÝ TIỀN PHẠT (Chiếm 1 cột) */}
                    <div className="lg:col-span-1">
                        <div className="bg-white rounded-2xl shadow-sm border p-6">
                            <h2 className="text-xl font-bold text-gray-800 mb-6 flex items-center gap-2"><AlertCircle className="w-6 h-6 text-red-500" /> Phạt Trả Muộn</h2>
                            {fines.length > 0 ? (
                                <div className="space-y-4">
                                    {fines.map(f => (
                                        <div key={f.id} className={`p-4 rounded-xl border ${f.status === 'UNPAID' ? 'bg-red-50 border-red-200' : 'bg-green-50 border-green-200'}`}>
                                            <div className="flex justify-between items-start mb-2">
                                                <span className="font-semibold text-gray-800 line-clamp-1">{f.reason}</span>
                                                <span className="font-bold text-lg whitespace-nowrap ml-2">{f.fineAmount.toLocaleString('vi-VN')} đ</span>
                                            </div>
                                            {f.status === 'UNPAID' ? (
                                                <button onClick={() => handlePayFine(f.id)} className="w-full bg-red-600 hover:bg-red-700 text-white py-2 rounded-lg font-medium text-sm transition-colors mt-2">
                                                    Thanh toán ngay
                                                </button>
                                            ) : (
                                                <div className="w-full bg-green-200 text-green-800 py-2 rounded-lg font-bold text-sm text-center mt-2">
                                                    Đã tất toán
                                                </div>
                                            )}
                                        </div>
                                    ))}
                                </div>
                            ) : (
                                <div className="text-center p-8 bg-gray-50 rounded-xl border border-dashed">
                                    <div className="text-green-500 text-4xl mb-2">🎉</div>
                                    <p className="text-gray-600 font-medium">Hồ sơ trong sạch!</p>
                                    <p className="text-sm text-gray-400 mt-1">Bro không có khoản nợ nào.</p>
                                </div>
                            )}
                        </div>
                    </div>
                </div>

            </div>
        </div>
    );
};

export default Dashboard;