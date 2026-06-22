import { useContext, useEffect, useState } from 'react';
import { AuthContext } from '../context/AuthContext';
import { LogOut, BookOpen, ChevronLeft, ChevronRight, Library, Filter } from 'lucide-react';
import api from '../services/api';
import BookCard from '../components/BookCard';

const Home = () => {
    const { user, logout } = useContext(AuthContext);
    
    const [books, setBooks] = useState([]);
    const [categories, setCategories] = useState([]);
    const [selectedCat, setSelectedCat] = useState('all');
    const [page, setPage] = useState(0); 
    const [totalPages, setTotalPages] = useState(0);
    const [loading, setLoading] = useState(true);

    // Kéo dữ liệu sách và danh mục
    useEffect(() => {
        let isMounted = true;
        const loadInitialData = async () => {
            try {
                const [catRes, bookRes] = await Promise.all([
                    api.get('/books/categories'),
                    api.get(`/books?page=${page}&size=8`)
                ]);
                if (isMounted) {
                    setCategories(catRes.data);
                    setBooks(bookRes.data.content);
                    setTotalPages(bookRes.data.page.totalPages);
                }
            } catch (err) {
                console.error("Lỗi tải trang:", err);
            } finally {
                if (isMounted) setLoading(false);
            }
        };
        loadInitialData();
        return () => { isMounted = false; };
    }, [page]); // Kéo lại dữ liệu khi đổi trang (ở chế độ All)

    // Hàm lọc sách khi chọn thể loại
    const handleCategorySelect = async (catId) => {
        setLoading(true);
        setSelectedCat(catId);
        setPage(0); // Reset về trang đầu
        try {
            const endpoint = catId === 'all' ? '/books?page=0&size=8' : `/books/category/${catId}?page=0&size=8`;
            const res = await api.get(endpoint);
            setBooks(res.data.content);
            setTotalPages(res.data.page.totalPages);
        } catch (err) {
            console.error("Lỗi lọc sách:", err);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="min-h-screen bg-gray-50 flex flex-col">
            <nav className="bg-white shadow-sm border-b px-6 py-4 flex justify-between items-center sticky top-0 z-50">
                <div className="flex items-center gap-2">
                    <div className="bg-blue-600 p-2 rounded-lg"><BookOpen className="text-white w-6 h-6" /></div>
                    <h1 className="text-2xl font-bold text-gray-800 tracking-tight">E-Library</h1>
                </div>
                <div className="flex items-center gap-6">
                    <div className="text-right hidden sm:block">
                        <p className="text-sm font-medium text-gray-900">Chào, {user?.fullName} 👋</p>
                    </div>
                    <button onClick={() => window.location.href = '/dashboard'} className="text-sm font-medium text-indigo-600 bg-indigo-50 px-4 py-2 rounded-lg hover:bg-indigo-100 transition-colors">Hồ sơ</button>
                    <button onClick={logout} className="flex items-center gap-2 bg-red-50 text-red-600 px-4 py-2 rounded-lg hover:bg-red-100 transition-colors font-medium text-sm"><LogOut className="w-4 h-4" /> Đăng xuất</button>
                </div>
            </nav>
            
            <main className="flex-grow max-w-7xl w-full mx-auto p-6 md:p-8 flex flex-col">
                <div className="flex items-center gap-3 mb-6">
                    <Library className="w-8 h-8 text-blue-600" />
                    <h2 className="text-3xl font-bold text-gray-800">Kho Sách</h2>
                </div>

                {/* THANH LỌC THỂ LOẠI */}
                <div className="flex items-center gap-3 mb-8 overflow-x-auto pb-2">
                    <Filter className="w-5 h-5 text-gray-400 flex-shrink-0" />
                    <button 
                        onClick={() => handleCategorySelect('all')}
                        className={`px-5 py-2 rounded-full text-sm font-medium transition-all flex-shrink-0 ${selectedCat === 'all' ? 'bg-blue-600 text-white shadow-md' : 'bg-white text-gray-600 border border-gray-200 hover:bg-gray-50'}`}
                    >Tất cả sách</button>
                    {categories.map(cat => (
                        <button 
                            key={cat.id}
                            onClick={() => handleCategorySelect(cat.id)}
                            className={`px-5 py-2 rounded-full text-sm font-medium transition-all whitespace-nowrap flex-shrink-0 ${selectedCat === cat.id ? 'bg-blue-600 text-white shadow-md' : 'bg-white text-gray-600 border border-gray-200 hover:bg-gray-50'}`}
                        >{cat.name}</button>
                    ))}
                </div>

                {loading ? (
                    <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
                        {[...Array(8)].map((_, i) => <div key={i} className="animate-pulse bg-white h-80 rounded-xl border border-gray-200"></div>)}
                    </div>
                ) : books.length > 0 ? (
                    <>
                        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6 mb-10">
                            {books.map(book => <BookCard key={book.id} book={book} />)}
                        </div>

                        {/* Phân trang */}
                        {totalPages > 1 && selectedCat === 'all' && (
                            <div className="mt-auto flex justify-center items-center gap-4 py-4">
                                <button onClick={() => {setLoading(true); setPage(p => p - 1);}} disabled={page === 0} className="flex items-center gap-1 px-4 py-2 bg-white border rounded-lg hover:bg-gray-50 disabled:opacity-50 text-sm font-medium"><ChevronLeft className="w-4 h-4" /> Trước</button>
                                <span className="text-sm font-medium text-gray-600 bg-white px-4 py-2 rounded-lg border">Trang {page + 1} / {totalPages}</span>
                                <button onClick={() => {setLoading(true); setPage(p => p + 1);}} disabled={page === totalPages - 1} className="flex items-center gap-1 px-4 py-2 bg-white border rounded-lg hover:bg-gray-50 disabled:opacity-50 text-sm font-medium">Sau <ChevronRight className="w-4 h-4" /></button>
                            </div>
                        )}
                    </>
                ) : (
                    <div className="flex flex-col items-center justify-center bg-white rounded-2xl border border-dashed p-16"><p className="text-gray-500">Không tìm thấy cuốn sách nào trong danh mục này.</p></div>
                )}
            </main>
        </div>
    );
};

export default Home;