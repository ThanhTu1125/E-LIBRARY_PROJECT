import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { ArrowLeft, BookOpen, Star, Calendar, BookmarkPlus } from 'lucide-react';
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

    useEffect(() => {
        let isMounted = true;

        const fetchBookData = async () => {
            try {
                // Gọi song song 2 API: Lấy chi tiết sách và Lấy Review
                const [bookRes, reviewsRes] = await Promise.all([
                    api.get(`/books/${id}`),
                    api.get(`/reviews/book/${id}`)
                ]);
                
                if (isMounted) {
                    setBook(bookRes.data);
                    setReviews(reviewsRes.data);
                }
            } catch (error) {
                console.error("Lỗi:", error);
                toast.error("Không thể tải thông tin sách!");
            } finally {
                if (isMounted) setLoading(false);
            }
        };

        fetchBookData();
        return () => { isMounted = false; };
    }, [id]);

    const handleBorrow = async (e) => {
        e.preventDefault();
        if (!copyId) {
            toast.warning("Vui lòng nhập ID bản sao vật lý của sách!");
            return;
        }

        setBorrowing(true);
        try {
            const response = await api.post('/borrowings', {
                bookCopyId: parseInt(copyId),
                durationDays: parseInt(duration)
            });
            toast.success("Mượn sách thành công! Dữ liệu đã ghi lên Blockchain.");
            setCopyId(''); // Reset form
            console.log("TxHash:", response.data.txHash);
        } catch (error) {
            toast.error(error.response?.data || "Mượn sách thất bại!");
        } finally {
            setBorrowing(false);
        }
    };

    if (loading) return <div className="min-h-screen flex items-center justify-center"><div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div></div>;
    if (!book) return <div className="text-center mt-20">Không tìm thấy sách.</div>;

    return (
        <div className="min-h-screen bg-gray-50 p-6 md:p-10">
            <div className="max-w-5xl mx-auto">
                {/* Nút quay lại */}
                <button onClick={() => navigate(-1)} className="flex items-center gap-2 text-gray-600 hover:text-blue-600 mb-6 transition-colors">
                    <ArrowLeft className="w-5 h-5" /> Quay lại kho sách
                </button>

                <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                    {/* CỘT TRÁI: Thông tin sách */}
                    <div className="lg:col-span-2 space-y-6">
                        <div className="bg-white rounded-2xl shadow-sm border border-gray-200 p-8 flex flex-col sm:flex-row gap-8">
                            <div className="w-full sm:w-48 h-64 flex-shrink-0 bg-blue-50 rounded-xl flex items-center justify-center border border-blue-100">
                                <BookOpen className="w-20 h-20 text-blue-300" />
                            </div>
                            
                            <div className="flex-grow">
                                <span className="text-sm font-bold text-blue-600 bg-blue-50 px-3 py-1 rounded-full uppercase">
                                    {book.category?.name}
                                </span>
                                <h1 className="text-3xl font-bold text-gray-900 mt-4 mb-2 leading-tight">{book.title}</h1>
                                
                                <div className="grid grid-cols-2 gap-4 mt-6 text-sm text-gray-600">
                                    <div><p className="text-gray-400">ISBN</p><p className="font-semibold text-gray-800">{book.isbn}</p></div>
                                    <div><p className="text-gray-400">Nhà Xuất Bản</p><p className="font-semibold text-gray-800">{book.publisher}</p></div>
                                    <div><p className="text-gray-400">Năm Xuất Bản</p><p className="font-semibold text-gray-800 flex items-center gap-1"><Calendar className="w-4 h-4"/>{book.publishYear}</p></div>
                                    <div><p className="text-gray-400">Lượt Xem</p><p className="font-semibold text-gray-800">{book.viewCount}</p></div>
                                </div>
                            </div>
                        </div>

                        {/* KHU VỰC REVIEW */}
                        <div className="bg-white rounded-2xl shadow-sm border border-gray-200 p-8">
                            <h2 className="text-xl font-bold text-gray-800 mb-6 flex items-center gap-2">
                                <Star className="w-6 h-6 text-yellow-400 fill-current" /> Đánh giá từ cộng đồng
                            </h2>
                            
                            {reviews.length > 0 ? (
                                <div className="space-y-4">
                                    {reviews.map((rev) => (
                                        <div key={rev.id} className="bg-gray-50 rounded-xl p-4 border border-gray-100">
                                            <div className="flex justify-between items-start mb-2">
                                                <span className="font-semibold text-gray-800">{rev.user?.fullName}</span>
                                                <span className="flex text-yellow-400">
                                                    {[...Array(5)].map((_, i) => (
                                                        <Star key={i} className={`w-4 h-4 ${i < rev.rating ? 'fill-current' : 'text-gray-300'}`} />
                                                    ))}
                                                </span>
                                            </div>
                                            <p className="text-gray-600 text-sm">{rev.comment}</p>
                                        </div>
                                    ))}
                                </div>
                            ) : (
                                <p className="text-gray-500 italic">Chưa có ai đánh giá cuốn sách này.</p>
                            )}
                        </div>
                    </div>

                    {/* CỘT PHẢI: Trạm Mượn Sách */}
                    <div className="lg:col-span-1">
                        <div className="bg-white rounded-2xl shadow-lg border-2 border-blue-100 p-6 sticky top-24">
                            <div className="flex items-center gap-3 mb-6">
                                <BookmarkPlus className="w-7 h-7 text-blue-600" />
                                <h3 className="text-xl font-bold text-gray-900">Trạm Mượn Sách</h3>
                            </div>
                            
                            <form onSubmit={handleBorrow} className="space-y-5">
                                <div>
                                    <label className="block text-sm font-semibold text-gray-700 mb-2">Mã Bản Sao Vật Lý (Copy ID)</label>
                                    <input 
                                        type="number" 
                                        min="1"
                                        placeholder="VD: 1, 2, 4..." 
                                        value={copyId}
                                        onChange={(e) => setCopyId(e.target.value)}
                                        className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500 outline-none transition-all"
                                        required
                                    />
                                    <p className="text-xs text-gray-500 mt-1">* ID in trên gáy sách vật lý</p>
                                </div>

                                <div>
                                    <label className="block text-sm font-semibold text-gray-700 mb-2">Thời gian mượn</label>
                                    <select 
                                        value={duration}
                                        onChange={(e) => setDuration(e.target.value)}
                                        className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500 outline-none transition-all"
                                    >
                                        <option value={7}>7 ngày</option>
                                        <option value={14}>14 ngày</option>
                                        <option value={30}>30 ngày</option>
                                    </select>
                                </div>

                                <button 
                                    type="submit" 
                                    disabled={borrowing}
                                    className="w-full bg-blue-600 hover:bg-blue-700 text-white font-bold py-3 px-4 rounded-xl transition-all duration-200 shadow-md hover:shadow-lg disabled:opacity-70 flex justify-center items-center"
                                >
                                    {borrowing ? (
                                        <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-white"></div>
                                    ) : "Xác Nhận Mượn"}
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