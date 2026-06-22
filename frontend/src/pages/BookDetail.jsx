import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { ArrowLeft, BookOpen, Star, Calendar, BookmarkPlus, Save, MessageSquare } from 'lucide-react';
import { toast } from 'react-toastify';
import api from '../services/api';

const BookDetail = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    
    const [book, setBook] = useState(null);
    const [reviews, setReviews] = useState([]);
    const [loading, setLoading] = useState(true);

    // State cho Form mượn sách
    const [copyId, setCopyId] = useState('');
    const [duration, setDuration] = useState(7);
    const [borrowing, setBorrowing] = useState(false);

    // State cho Form Tương tác mới
    const [comment, setComment] = useState('');
    const [rating, setRating] = useState(5);
    const [progress, setProgress] = useState(1);
    
    // Biến cò mồi để load lại danh sách Review mà không vi phạm luật Hook
    const [refreshKey, setRefreshKey] = useState(0);

    useEffect(() => {
        let isMounted = true;
        const fetchData = async () => {
            try {
                const [bookRes, reviewsRes] = await Promise.all([
                    api.get(`/books/${id}`),
                    api.get(`/reviews/book/${id}`)
                ]);
                if (isMounted) {
                    setBook(bookRes.data);
                    setReviews(reviewsRes.data);
                }
            } catch (err) {
                console.error("Lỗi tải trang:", err);
                toast.error("Không thể tải thông tin sách!");
            } finally {
                if (isMounted) setLoading(false);
            }
        };

        fetchData();
        return () => { isMounted = false; };
    }, [id, refreshKey]); // Chạy lại mỗi khi ID sách đổi hoặc cò mồi bị kích hoạt

    const handleBorrow = async (e) => {
        e.preventDefault();
        if (!copyId) { toast.warning("Vui lòng nhập ID bản sao vật lý!"); return; }
        setBorrowing(true);
        try {
            await api.post('/borrowings', { bookCopyId: parseInt(copyId), durationDays: parseInt(duration) });
            toast.success("Mượn sách thành công! Dữ liệu đã ghi lên Blockchain.");
            setCopyId('');
        } catch (err) {
            toast.error(err.response?.data || "Mượn sách thất bại!");
        } finally {
            setBorrowing(false);
        }
    };

    const handleSaveProgress = async () => {
        if (!progress || progress < 1) { toast.warning("Số trang không hợp lệ!"); return; }
        try {
            await api.post('/history', { bookId: parseInt(id), lastReadPage: parseInt(progress) });
            toast.info("Đã cập nhật tiến độ đọc!");
        } catch { // Đã xóa chữ (error) cho chuẩn luật Linter
            toast.error("Lỗi khi lưu tiến độ!");
        }
    };

    const handleAddReview = async () => {
        if (!comment.trim()) { toast.warning("Vui lòng nhập nội dung đánh giá!"); return; }
        try {
            await api.post('/reviews', { bookId: parseInt(id), rating: parseInt(rating), comment });
            toast.success("Cảm ơn bro đã đánh giá!");
            setComment('');
            setRefreshKey(prev => prev + 1); // Kích hoạt cò mồi để kéo review mới về
        } catch (err) {
            toast.error(err.response?.data || "Lỗi khi gửi đánh giá!");
        }
    };

    if (loading) return <div className="min-h-screen flex items-center justify-center"><div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div></div>;
    if (!book) return <div className="text-center mt-20">Không tìm thấy sách.</div>;

    return (
        <div className="min-h-screen bg-gray-50 p-6 md:p-10">
            <div className="max-w-6xl mx-auto">
                <button onClick={() => navigate(-1)} className="flex items-center gap-2 text-gray-600 hover:text-blue-600 mb-6 transition-colors font-medium">
                    <ArrowLeft className="w-5 h-5" /> Quay lại
                </button>

                <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                    <div className="lg:col-span-2 space-y-6">
                        {/* Box 1: Thông tin */}
                        <div className="bg-white rounded-2xl shadow-sm border p-8 flex flex-col sm:flex-row gap-8">
                            <div className="w-full sm:w-48 h-64 flex-shrink-0 bg-blue-50 rounded-xl flex items-center justify-center">
                                <BookOpen className="w-20 h-20 text-blue-300" />
                            </div>
                            <div className="flex-grow">
                                <span className="text-xs font-bold text-blue-600 bg-blue-50 px-3 py-1 rounded-full uppercase">{book.category?.name}</span>
                                <h1 className="text-3xl font-bold text-gray-900 mt-4 mb-2">{book.title}</h1>
                                <p className="text-sm text-gray-500 mb-4">NXB: <span className="font-medium text-gray-700">{book.publisher}</span></p>
                                <div className="flex gap-6 text-sm text-gray-600 mt-6 border-t pt-4">
                                    <span className="flex items-center gap-2"><Calendar className="w-4 h-4"/> {book.publishYear}</span>
                                    <span className="flex items-center gap-2 bg-gray-100 px-2 py-1 rounded-md font-semibold">{book.viewCount} Views</span>
                                </div>
                            </div>
                        </div>

                        {/* Box 2: Form Tương Tác */}
                        <div className="bg-white rounded-2xl shadow-sm border p-8 grid grid-cols-1 md:grid-cols-2 gap-8">
                            <div>
                                <h3 className="font-bold text-gray-800 mb-4 flex items-center gap-2"><Save className="w-5 h-5 text-green-600"/> Cập nhật tiến độ</h3>
                                <div className="flex gap-3">
                                    <input type="number" min="1" value={progress} onChange={(e) => setProgress(e.target.value)} className="w-24 px-4 py-2 border rounded-lg outline-blue-500" placeholder="Trang"/>
                                    <button onClick={handleSaveProgress} className="bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 font-medium whitespace-nowrap">Lưu mốc</button>
                                </div>
                            </div>
                            <div>
                                <h3 className="font-bold text-gray-800 mb-4 flex items-center gap-2"><MessageSquare className="w-5 h-5 text-blue-600"/> Viết nhận xét</h3>
                                <textarea className="w-full p-3 border rounded-lg mb-3 outline-blue-500 text-sm" rows="2" placeholder="Sách hay không bro?" value={comment} onChange={(e) => setComment(e.target.value)}></textarea>
                                <div className="flex justify-between items-center">
                                    <select value={rating} onChange={(e) => setRating(e.target.value)} className="p-2 border rounded-lg text-sm bg-gray-50">
                                        {[5,4,3,2,1].map(n => <option key={n} value={n}>{n} 🌟 Cực cháy</option>)}
                                    </select>
                                    <button onClick={handleAddReview} className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 font-medium text-sm">Gửi luôn</button>
                                </div>
                            </div>
                        </div>

                        {/* Box 3: Danh sách Review */}
                        <div className="bg-white rounded-2xl shadow-sm border p-8">
                            <h2 className="text-xl font-bold text-gray-800 mb-6 flex items-center gap-2"><Star className="w-6 h-6 text-yellow-400 fill-current" /> Đánh giá từ cộng đồng</h2>
                            {reviews.length > 0 ? (
                                <div className="space-y-4">
                                    {reviews.map(rev => (
                                        <div key={rev.id} className="bg-gray-50 rounded-xl p-4 border border-gray-100">
                                            <div className="flex justify-between items-center mb-2">
                                                <span className="font-semibold text-gray-800">{rev.user?.fullName}</span>
                                                <span className="flex text-yellow-400">
                                                    {[...Array(5)].map((_, i) => <Star key={i} className={`w-4 h-4 ${i < rev.rating ? 'fill-current' : 'text-gray-300'}`} />)}
                                                </span>
                                            </div>
                                            <p className="text-gray-600 text-sm">{rev.comment}</p>
                                        </div>
                                    ))}
                                </div>
                            ) : <p className="text-gray-500 italic">Bro hãy là người đầu tiên bóc tem cuốn sách này đi!</p>}
                        </div>
                    </div>

                    {/* Cột Trạm Mượn */}
                    <div className="lg:col-span-1">
                        <div className="bg-white rounded-2xl shadow-lg border-2 border-blue-100 p-6 sticky top-24">
                            <h3 className="text-xl font-bold text-gray-900 mb-6 flex items-center gap-2"><BookmarkPlus className="w-6 h-6 text-blue-600"/> Trạm Mượn Sách</h3>
                            <form onSubmit={handleBorrow} className="space-y-4">
                                <div>
                                    <label className="block text-sm font-semibold text-gray-700 mb-2">Mã sách vật lý (Copy ID)</label>
                                    <input type="number" min="1" value={copyId} onChange={(e) => setCopyId(e.target.value)} className="w-full px-4 py-2 border rounded-lg focus:ring-2 outline-none" required />
                                </div>
                                <div>
                                    <label className="block text-sm font-semibold text-gray-700 mb-2">Thời hạn mượn</label>
                                    <select value={duration} onChange={(e) => setDuration(e.target.value)} className="w-full px-4 py-2 border rounded-lg focus:ring-2 outline-none">
                                        <option value={7}>7 ngày</option><option value={14}>14 ngày</option><option value={30}>30 ngày</option>
                                    </select>
                                </div>
                                <button type="submit" disabled={borrowing} className="w-full bg-blue-600 text-white font-bold py-3 rounded-xl hover:bg-blue-700 disabled:opacity-70 mt-4">
                                    {borrowing ? "Đang xử lý..." : "Chốt Đơn"}
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default BookDetail;