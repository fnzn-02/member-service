import { useState } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Header from './components/Header';
import Login from './pages/Login';
import Signup from './pages/Signup';
import MyPage from './pages/MyPage';
import ForgotPassword from './pages/ForgotPassword';

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(!!localStorage.getItem('token'));

  return (
    <BrowserRouter>
      <div className="min-h-screen bg-neutral-50 dark:bg-netflixBlack text-neutral-900 dark:text-white font-sans transition-colors duration-300">
        <Header isAuthenticated={isAuthenticated} setIsAuthenticated={setIsAuthenticated} />
        <main className="pt-24 pb-12 px-4 sm:px-6 max-w-7xl mx-auto w-full flex flex-col items-center justify-center min-h-[calc(100vh-80px)]">
          <Routes>
            <Route
              path="/"
              element={
                isAuthenticated ? (
                  <Navigate to="/mypage" />
                ) : (
                  <div className="text-center animate-fade-in-up mt-20">
                    <h1 className="text-5xl sm:text-7xl font-extrabold tracking-tight mb-6">
                      당신의 일상을 켜다,<br />
                      <span className="text-tossBlue">OnAir</span>
                    </h1>
                    <p className="text-lg text-gray-500 dark:text-gray-400 mb-10">
                      압도적인 디자인과 성능을 지금 바로 경험해보세요.
                    </p>
                  </div>
                )
              }
            />
            <Route
              path="/login"
              element={isAuthenticated ? <Navigate to="/mypage" /> : <Login setIsAuthenticated={setIsAuthenticated} />}
            />
            <Route
              path="/signup"
              element={isAuthenticated ? <Navigate to="/mypage" /> : <Signup />}
            />
            <Route
              path="/mypage"
              element={isAuthenticated ? <MyPage setIsAuthenticated={setIsAuthenticated} /> : <Navigate to="/login" />}
            />
            <Route
              path="/forgot-password"
              element={isAuthenticated ? <Navigate to="/mypage" /> : <ForgotPassword />}
            />
          </Routes>
        </main>
      </div>
    </BrowserRouter>
  );
}

export default App;
