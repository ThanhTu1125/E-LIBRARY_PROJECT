import { Book, Eye, Calendar } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

const BookCard = ({ book }) => {
    const navigate = useNavigate();

    return (
        <div 
            onClick={() => navigate(`/books/${book.id}`)}
            className="bg-white border border-gray-200 rounded-xl overflow-hidden shadow-sm hover:shadow-lg hover:-translate-y-1 transition-all duration-300 group cursor-pointer"
        >
            <div className="h-48 bg-gradient-to-br from-blue-50 to-indigo-50 flex items-center justify-center border-b border-gray-100">
                {book.coverImage ? (
                    <img src={book.coverImage} alt={book.title} className="w-full h-full object-cover" />
                ) : (
                    <Book className="w-16 h-16 text-blue-300 group-hover:text-blue-500 transition-colors" />
                )}
            </div>
            
            <div className="p-5">
                <span className="text-xs font-bold text-indigo-600 uppercase tracking-wider bg-indigo-50 px-2 py-1 rounded-md">
                    {book.category?.name || 'Chưa phân loại'}
                </span>
                
                <h3 className="text-lg font-bold text-gray-900 mt-3 mb-1 line-clamp-2 leading-tight" title={book.title}>
                    {book.title}
                </h3>
                
                <p className="text-sm text-gray-500 line-clamp-1 mb-4">
                    Nhà XB: <span className="font-medium text-gray-700">{book.publisher || 'Đang cập nhật'}</span>
                </p>
                
                <div className="flex items-center justify-between text-xs text-gray-400 border-t pt-3">
                    <span className="flex items-center gap-1.5">
                        <Calendar className="w-4 h-4" /> 
                        {book.publishYear || 'N/A'}
                    </span>
                    <span className="flex items-center gap-1.5 bg-gray-50 px-2 py-1 rounded-md">
                        <Eye className="w-4 h-4" /> 
                        <span className="font-semibold text-gray-600">{book.viewCount}</span>
                    </span>
                </div>
            </div>
        </div>
    );
};

export default BookCard;