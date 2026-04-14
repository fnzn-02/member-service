import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { sendPasswordResetCode, resetPassword } from '../api/member';

const ForgotPassword = () => {
  const [email, setEmail] = useState('');
  const [code, setCode] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [newPasswordConfirm, setNewPasswordConfirm] = useState('');
  const [isCodeSent, setIsCodeSent] = useState(false);
  const [isLoading, setIsLoading] = useState(false);

  const navigate = useNavigate();

  const handleSendCode = async () => {
    if (!email) return alert('이메일을 입력해주세요.');
    setIsLoading(true);
    try {
      await sendPasswordResetCode(email);
      alert(isCodeSent ? '인증번호가 재발송되었습니다.' : '인증번호가 발송되었습니다.');
      setIsCodeSent(true);
    } catch (error) {
      alert(error.response?.data?.errorMessage || '인증번호 발송에 실패했습니다. 이메일을 확인해주세요.');
    } finally {
      setIsLoading(false);
    }
  };

  const handleResetPassword = async (e) => {
    e.preventDefault();

    if (!isCodeSent) return alert('먼저 인증번호를 받아주세요.');
    if (!code || code.length !== 6) return alert('인증번호 6자리를 입력해주세요.');
    if (newPassword !== newPasswordConfirm) return alert('새 비밀번호가 일치하지 않습니다.');

    const passwordRegex = /^(?=.*[!@#$%^&*(),.?":{}|<>]).{8,}$/;
    if (!passwordRegex.test(newPassword)) {
      return alert('새 비밀번호는 8자 이상이어야 하며, 특수문자를 최소 1개 포함해야 합니다.');
    }

    try {
      await resetPassword(email, code, newPassword);
      alert('비밀번호가 성공적으로 변경되었습니다. 새 비밀번호로 로그인해주세요.');
      navigate('/login');
    } catch (error) {
      alert(error.response?.data?.errorMessage || '비밀번호 재설정에 실패했습니다. 인증번호를 확인해주세요.');
    }
  };

  return (
    <div className="w-full flex justify-center items-center animate-fade-in-up mt-10">
      <div className="w-full max-w-md bg-white dark:bg-netflixCard p-10 rounded-[2.5rem] shadow-[0_10px_40px_rgba(0,0,0,0.08)] dark:shadow-none border border-gray-100 dark:border-gray-800 transition-all duration-300">
        <h2 className="text-3xl font-extrabold text-center mb-2 tracking-tight">
          비밀번호 찾기
        </h2>
        <p className="text-sm text-center text-gray-500 dark:text-gray-400 mb-10">
          가입된 이메일로 인증번호를 받아 비밀번호를 재설정합니다.
        </p>

        <form onSubmit={handleResetPassword} className="space-y-5">
          <div>
            <label className="block text-sm font-semibold text-gray-700 dark:text-gray-300 mb-2 pl-1">이메일</label>
            <div className="flex gap-2">
              <input
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="w-full px-5 py-4 rounded-2xl bg-gray-50 dark:bg-netflixBlack border border-transparent focus:border-tossBlue focus:bg-white dark:focus:bg-netflixBlack focus:ring-4 focus:ring-tossBlue/20 transition-all outline-none dark:text-white font-medium placeholder-gray-400"
                placeholder="onair@example.com"
              />
              <button
                type="button"
                onClick={handleSendCode}
                disabled={isLoading}
                className="whitespace-nowrap px-4 py-4 rounded-2xl bg-gray-900 dark:bg-white text-white dark:text-gray-900 font-bold hover:brightness-110 disabled:opacity-50 transition-all"
              >
                {isCodeSent ? '재발송' : '인증 받기'}
              </button>
            </div>
          </div>

          {isCodeSent && (
            <div className="space-y-5 animate-fade-in-up">
              <div>
                <label className="block text-sm font-semibold text-tossBlue mb-2 pl-1">인증번호 6자리</label>
                <input
                  type="text"
                  value={code}
                  onChange={(e) => setCode(e.target.value)}
                  maxLength={6}
                  className="w-full px-5 py-4 rounded-2xl bg-blue-50/50 dark:bg-blue-900/20 border border-tossBlue/30 focus:border-tossBlue transition-all outline-none dark:text-white font-bold tracking-widest text-center text-lg"
                  placeholder="000000"
                />
              </div>
              <div>
                <label className="block text-sm font-semibold text-gray-700 dark:text-gray-300 mb-2 pl-1">새 비밀번호</label>
                <input
                  type="password"
                  value={newPassword}
                  onChange={(e) => setNewPassword(e.target.value)}
                  className="w-full px-5 py-4 rounded-2xl bg-gray-50 dark:bg-netflixBlack border border-transparent focus:border-tossBlue focus:bg-white dark:focus:bg-netflixBlack focus:ring-4 focus:ring-tossBlue/20 transition-all outline-none dark:text-white font-medium placeholder-gray-400"
                  placeholder="특수문자 포함 8자 이상"
                />
              </div>
              <div>
                <label className="block text-sm font-semibold text-gray-700 dark:text-gray-300 mb-2 pl-1">새 비밀번호 확인</label>
                <input
                  type="password"
                  value={newPasswordConfirm}
                  onChange={(e) => setNewPasswordConfirm(e.target.value)}
                  className="w-full px-5 py-4 rounded-2xl bg-gray-50 dark:bg-netflixBlack border border-transparent focus:border-tossBlue focus:bg-white dark:focus:bg-netflixBlack focus:ring-4 focus:ring-tossBlue/20 transition-all outline-none dark:text-white font-medium placeholder-gray-400"
                  placeholder="한 번 더 입력"
                />
              </div>
              <button
                type="submit"
                className="w-full py-4 mt-2 bg-tossBlue hover:bg-tossBlueHover text-white text-lg font-bold rounded-2xl shadow-lg shadow-tossBlue/30 hover:-translate-y-1 transition-all duration-200"
              >
                비밀번호 재설정하기
              </button>
            </div>
          )}
        </form>

        <div className="mt-8 text-center text-sm text-gray-500 dark:text-gray-400">
          <Link to="/login" className="text-tossBlue font-bold hover:underline transition-all">
            로그인으로 돌아가기
          </Link>
        </div>
      </div>
    </div>
  );
};

export default ForgotPassword;
