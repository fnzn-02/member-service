import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getMyInfo, updateNickname, updatePassword, deleteMember } from '../api/member';

const MyPage = ({ setIsAuthenticated }) => {
  const [user, setUser] = useState({ email: '', nickname: '' });
  const [newNickname, setNewNickname] = useState('');
  const [passwords, setPasswords] = useState({ current: '', next: '', confirm: '' });
  const [isLoading, setIsLoading] = useState(true);

  const navigate = useNavigate();

  useEffect(() => {
    const init = async () => {
      try {
        const data = await getMyInfo();
        setUser(data);
        setNewNickname(data.nickname || '');
      } catch (err) {
        console.error(err);
        alert('로그인이 만료되었거나 정보가 없습니다. 다시 로그인해주세요.');
        handleLogout();
      } finally {
        setIsLoading(false);
      }
    };
    init();
  }, []);

  const handleLogout = () => {
    localStorage.removeItem('token');
    setIsAuthenticated(false);
    navigate('/login');
  };

  const handleNicknameUpdate = async () => {
    if (!newNickname) return alert('변경할 닉네임을 입력해주세요.');
    try {
      await updateNickname(newNickname);
      alert('닉네임이 성공적으로 변경되었습니다!');
      setUser({ ...user, nickname: newNickname });
    } catch (error) {
      alert('닉네임 변경에 실패했습니다.');
    }
  };

  const handlePasswordUpdate = async () => {
    const { current, next, confirm } = passwords;

    if (!current || !next || !confirm) {
      return alert('현재 비밀번호, 새 비밀번호, 확인 칸을 모두 입력해주세요.');
    }
    if (next !== confirm) {
      return alert('새 비밀번호와 비밀번호 확인이 일치하지 않습니다.');
    }
    const passwordRegex = /^(?=.*[!@#$%^&*(),.?":{}|<>]).{8,}$/;
    if (!passwordRegex.test(next)) {
      return alert('새 비밀번호는 8자 이상이어야 하며, 특수문자를 최소 1개 포함해야 합니다.');
    }
    if (current === next) {
      return alert('현재 비밀번호와 동일한 비밀번호로 변경할 수 없습니다.');
    }

    try {
      await updatePassword(current, next);
      alert('비밀번호가 성공적으로 변경되었습니다. 다시 로그인해주세요.');
      handleLogout();
    } catch (error) {
      const msg = error.response?.data?.errorMessage || '현재 비밀번호가 올바르지 않거나 변경에 실패했습니다.';
      alert(msg);
    }
  };

  const handleDeleteAccount = async () => {
    if (!window.confirm('정말로 탈퇴하시겠습니까? 모든 데이터가 삭제됩니다.')) return;
    try {
      await deleteMember();
      alert('탈퇴가 완료되었습니다. 이용해 주셔서 감사합니다.');
      handleLogout();
    } catch (error) {
      alert('탈퇴 처리 중 오류가 발생했습니다.');
    }
  };

  if (isLoading) {
    return <div className="text-center mt-20 text-gray-500 dark:text-gray-400 font-bold animate-pulse">유저 정보를 불러오는 중입니다...</div>;
  }

  return (
    <div className="w-full flex justify-center items-center animate-fade-in-up mt-6 mb-10">
      <div className="w-full max-w-md bg-white dark:bg-netflixCard p-10 rounded-[2.5rem] shadow-[0_10px_40px_rgba(0,0,0,0.08)] dark:shadow-none border border-gray-100 dark:border-gray-800 transition-all duration-300">
        <h2 className="text-3xl font-extrabold tracking-tight text-center mb-8">마이페이지</h2>

        <div className="mb-6 p-5 bg-gray-50 dark:bg-netflixBlack rounded-2xl border border-gray-100 dark:border-gray-800">
          <label className="block text-xs font-semibold text-gray-500 dark:text-gray-400 mb-1">가입된 이메일</label>
          <div className="text-lg font-bold text-gray-900 dark:text-white">{user.email}</div>
        </div>

        <div className="space-y-3 mb-8">
          <label className="block text-sm font-semibold text-gray-700 dark:text-gray-300 pl-1">닉네임 변경</label>
          <div className="flex gap-2">
            <input
              type="text"
              value={newNickname}
              onChange={(e) => setNewNickname(e.target.value)}
              className="w-full px-5 py-4 rounded-2xl bg-gray-50 dark:bg-netflixBlack border border-transparent focus:border-tossBlue outline-none dark:text-white font-medium"
              placeholder="새 닉네임"
            />
            <button
              onClick={handleNicknameUpdate}
              className="whitespace-nowrap px-6 py-4 rounded-2xl bg-gray-900 dark:bg-white text-white dark:text-gray-900 font-bold hover:brightness-110 transition-all"
            >
              변경
            </button>
          </div>
        </div>

        <div className="space-y-3 mb-8">
          <label className="block text-sm font-semibold text-gray-700 dark:text-gray-300 pl-1">비밀번호 변경</label>
          <input
            type="password"
            value={passwords.current}
            onChange={(e) => setPasswords({ ...passwords, current: e.target.value })}
            className="w-full px-5 py-4 rounded-2xl bg-gray-50 dark:bg-netflixBlack border border-transparent focus:border-tossBlue outline-none dark:text-white font-medium"
            placeholder="현재 비밀번호"
          />
          <input
            type="password"
            value={passwords.next}
            onChange={(e) => setPasswords({ ...passwords, next: e.target.value })}
            className="w-full px-5 py-4 rounded-2xl bg-gray-50 dark:bg-netflixBlack border border-transparent focus:border-tossBlue outline-none dark:text-white font-medium"
            placeholder="새 비밀번호"
          />
          <input
            type="password"
            value={passwords.confirm}
            onChange={(e) => setPasswords({ ...passwords, confirm: e.target.value })}
            className="w-full px-5 py-4 rounded-2xl bg-gray-50 dark:bg-netflixBlack border border-transparent focus:border-tossBlue outline-none dark:text-white font-medium"
            placeholder="새 비밀번호 확인"
          />
          <button
            onClick={handlePasswordUpdate}
            className="w-full py-4 bg-tossBlue hover:bg-tossBlueHover text-white text-lg font-bold rounded-2xl shadow-lg shadow-tossBlue/30 transition-all"
          >
            비밀번호 변경하기
          </button>
        </div>

        <div className="space-y-4 pt-4 border-t border-gray-100 dark:border-gray-800">
          <button
            onClick={handleLogout}
            className="w-full py-4 text-red-500 font-bold rounded-2xl bg-red-50 dark:bg-red-900/10 hover:bg-red-100 dark:hover:bg-red-900/30 transition-all"
          >
            로그아웃
          </button>
          <button
            onClick={handleDeleteAccount}
            className="w-full py-2 text-gray-400 text-sm underline hover:text-gray-600 dark:hover:text-gray-200 transition-colors"
          >
            회원 탈퇴하기
          </button>
        </div>
      </div>
    </div>
  );
};

export default MyPage;
