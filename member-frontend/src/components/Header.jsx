import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { SunIcon, MoonIcon } from '@heroicons/react/24/outline';

const Header = ({ isAuthenticated, setIsAuthenticated }) => {
  const [isDark, setIsDark] = useState(false);
  const navigate = useNavigate();

  const toggleDarkMode = () => {
    document.documentElement.classList.toggle('dark');
    setIsDark(!isDark);
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    setIsAuthenticated(false);
    navigate('/login');
  };

  return (
    <header className="fixed top-0 left-0 w-full z-50 transition-all duration-300 bg-white/70 dark:bg-netflixBlack/70 backdrop-blur-md border-b border-gray-200 dark:border-gray-800">
      <div className="max-w-7xl mx-auto px-6 h-20 flex items-center justify-between">
        <Link to="/" className="text-2xl font-extrabold tracking-tight text-tossBlue hover:text-tossBlueHover transition-colors">
          OnAir
        </Link>
        <div className="flex items-center space-x-6">
          {isAuthenticated ? (
            <>
              <Link to="/mypage" className="text-gray-700 dark:text-gray-300 font-medium hover:text-tossBlue dark:hover:text-white transition-colors">
                마이페이지
              </Link>
              <button
                onClick={handleLogout}
                className="hidden sm:inline-block px-5 py-2.5 rounded-full bg-gray-100 dark:bg-gray-800 text-gray-700 dark:text-gray-300 font-bold hover:bg-gray-200 dark:hover:bg-gray-700 transition-all duration-200"
              >
                로그아웃
              </button>
            </>
          ) : (
            <>
              <Link to="/login" className="text-gray-700 dark:text-gray-300 font-medium hover:text-tossBlue dark:hover:text-white transition-colors">
                로그인
              </Link>
              <Link to="/signup" className="hidden sm:inline-block px-6 py-2.5 rounded-full bg-tossBlue text-white font-bold shadow-lg shadow-tossBlue/30 hover:bg-tossBlueHover hover:-translate-y-0.5 transition-all duration-200">
                회원가입
              </Link>
            </>
          )}
          <div className="w-px h-6 bg-gray-300 dark:bg-gray-700" />
          <button
            onClick={toggleDarkMode}
            className="p-2 rounded-full hover:bg-gray-100 dark:hover:bg-gray-800 text-gray-600 dark:text-gray-400 transition-colors"
          >
            {isDark ? (
              <SunIcon className="w-6 h-6 text-yellow-400" />
            ) : (
              <MoonIcon className="w-6 h-6" />
            )}
          </button>
        </div>
      </div>
    </header>
  );
};

export default Header;
