import { useContext, useEffect, useState } from 'react';
import { AuthContext } from '../context/AuthContext';
import { LogOut, BookOpen, ChevronLeft, ChevronRight, Library } from 'lucide-react';
import api from '../services/api';
import BookCard from '../components/BookCard';

const Home = () => {
    const { user, logout } = useContext(AuthContext);
    
    const [books, setBooks] = useState([]);
    const [page, setPage] = useState(0); 
    const [totalPages, setTotalPages] = useState(0);
    // Khởi tạo ban đầu là true để lúc mới vào web tự hiện skeleton
    const [loading, setLoading] = useState(true); 

    useEffect(() => {
        let isMounted = true; // Biến cờ hiệu để dọn dẹp memory leak

        const fetchBooks = async () => {
            try {
                const response = await api.get(`/books?page=${page}&size=8`);
                if (isMounted) {
                    setBooks(response.data.content);
                    setTotalPages(response.data.page.totalPages);
                }
            } catch (error) {
                console.error("Lỗi khi kéo danh sách sách:", error);
            } finally {
                // Chỉ set trạng thái sau khi API đã trả về (Bất đồng bộ)
                if (isMounted) {
                    setLoading(false);
                }
            }
        };

        fetchBooks();

        // Hàm cleanup dọn dẹp khi Component bị hủy
        return () => {
            isMounted = false;
        };
    }, [page]);

    // Xử lý sự kiện bấm nút chuyển trang chuẩn tư duy React mới
    const handlePageChange = (newPage) => {
        setLoading(true); // Đặt loading = true ở sự kiện click, Linter sẽ không chửi nữa
        setPage(newPage);
    };

    return (
        <div className="min-h-screen bg-gray-50 flex flex-col">
            <nav className="bg-white shadow-sm border-b px-6 py-4 flex justify-between items-center sticky top-0 z-50">
                <div className="flex items-center gap-2">
                    <div className="bg-blue-600 p-2 rounded-lg">
                        <BookOpen className="text-white w-6 h-6" />
                    </div>
                    <h1 className="text-2xl font-bold text-gray-800 tracking-tight">E-Library</h1>
                </div>
                
                <div className="flex items-center gap-6">
                    <div className="text-right hidden sm:block">
                        <p className="text-sm font-medium text-gray-900">Chào, {user?.fullName} 👋</p>
                        <p className="text-xs text-gray-500 capitalize">{user?.role?.replace('ROLE_', '')}</p>
                    </div>
                    {/* Nút vào Dashboard */}
                    <button 
                        onClick={() => window.location.href = '/dashboard'}
                        className="flex items-center gap-2 bg-indigo-50 text-indigo-600 px-4 py-2 rounded-lg hover:bg-indigo-100 transition-colors font-medium text-sm border border-indigo-100"
                    >
                        Hồ sơ
                    </button>
                    <button 
                        onClick={logout}
                        className="flex items-center gap-2 bg-red-50 text-red-600 px-4 py-2 rounded-lg hover:bg-red-100 transition-colors font-medium text-sm"
                    >
                        <LogOut className="w-4 h-4" /> Đăng xuất
                    </button>
                </div>
            </nav>
            
            <main className="flex-grow max-w-7xl w-full mx-auto p-6 md:p-8 flex flex-col">
                <div className="flex items-center gap-3 mb-8">
                    <Library className="w-8 h-8 text-blue-600" />
                    <h2 className="text-3xl font-bold text-gray-800">Kho Sách Mới Nhất</h2>
                </div>

                {loading ? (
                    <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
                        {[...Array(8)].map((_, i) => (
                            <div key={i} className="animate-pulse bg-white h-80 rounded-xl border border-gray-200"></div>
                        ))}
                    </div>
                ) : books.length > 0 ? (
                    <>
                        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6 mb-10">
                            {books.map((book) => (
                                <BookCard key={book.id} book={book} />
                            ))}
                        </div>

                        {totalPages > 1 && (
                            <div className="mt-auto flex justify-center items-center gap-4 py-4">
                                <button
                                    onClick={() => handlePageChange(page - 1)}
                                    disabled={page === 0}
                                    className="flex items-center gap-1 px-4 py-2 bg-white border border-gray-300 rounded-lg hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed transition-colors text-sm font-medium"
                                >
                                    <ChevronLeft className="w-4 h-4" /> Trước
                                </button>
                                
                                <span className="text-sm font-medium text-gray-600 bg-white px-4 py-2 rounded-lg border border-gray-200 shadow-sm">
                                    Trang {page + 1} / {totalPages}
                                </span>
                                
                                <button
                                    onClick={() => handlePageChange(page + 1)}
                                    disabled={page === totalPages - 1}
                                    className="flex items-center gap-1 px-4 py-2 bg-white border border-gray-300 rounded-lg hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed transition-colors text-sm font-medium"
                                >
                                    Sau <ChevronRight className="w-4 h-4" />
                                </button>
                            </div>
                        )}
                    </>
                ) : (
                    <div className="flex flex-col items-center justify-center bg-white rounded-2xl border border-dashed border-gray-300 p-16">
                        <BookOpen className="w-16 h-16 text-gray-300 mb-4" />
                        <h3 className="text-xl font-bold text-gray-700">Thư viện đang trống</h3>
                        <p className="text-gray-500 mt-2">Chưa có cuốn sách nào được thêm vào hệ thống.</p>
                    </div>
                )}
            </main>
        </div>
    );
};

export default Home;