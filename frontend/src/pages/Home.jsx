import { useContext } from 'react';
import { AuthContext } from '../context/AuthContext';
import { LogOut, BookOpen } from 'lucide-react';

const Home = () => {
    const { user, logout } = useContext(AuthContext);

    return (
        <div className="min-h-screen bg-gray-50">
            {/* Thanh Điều Hướng (Navbar) */}
            <nav className="bg-white shadow-sm border-b px-6 py-4 flex justify-between items-center sticky top-0 z-50">
                <div className="flex items-center gap-2">
                    <div className="bg-blue-600 p-2 rounded-lg">
                        <BookOpen className="text-white w-6 h-6" />
                    </div>
                    <h1 className="text-2xl font-bold text-gray-800 tracking-tight">E-Library</h1>
                </div>
                
                <div className="flex items-center gap-6">
                    {/* Hiển thị tên người dùng lấy từ Context */}
                    <div className="text-right hidden sm:block">
                        <p className="text-sm font-medium text-gray-900">Chào, {user?.fullName} 👋</p>
                        <p className="text-xs text-gray-500 capitalize">{user?.role?.replace('ROLE_', '')}</p>
                    </div>
                    
                    <button 
                        onClick={logout}
                        className="flex items-center gap-2 bg-red-50 text-red-600 px-4 py-2 rounded-lg hover:bg-red-100 transition-colors font-medium text-sm"
                    >
                        <LogOut className="w-4 h-4" /> Đăng xuất
                    </button>
                </div>
            </nav>
            
            {/* Khu vực chứa sách sau này */}
            <main className="max-w-7xl mx-auto p-8">
                <div className="bg-white rounded-xl shadow-sm border p-8 text-center border-dashed border-2 border-gray-300">
                    <h2 className="text-2xl font-bold text-gray-800 mb-2">Trang chủ Thư viện số</h2>
                    <p className="text-gray-500">Danh sách các cuốn sách sẽ sớm được tải về từ Spring Boot và hiển thị tại đây!</p>
                </div>
            </main>
        </div>
    );
};

export default Home;