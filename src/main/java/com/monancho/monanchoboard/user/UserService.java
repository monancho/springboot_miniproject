package com.monancho.monanchoboard.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.monancho.monanchoboard.DataNotFoundException;

@Service
public class UserService {

    private final UserSecurityService userSecurityService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

    UserService(UserSecurityService userSecurityService) {
        this.userSecurityService = userSecurityService;
    }
	
	public SiteUser create(String username, String email, String password) {
		SiteUser user = new SiteUser();
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(password));
		userRepository.save(user);
		
		return user;
		
	}
	
	// 유저 아이디로 엔티티 가져오기
	public SiteUser getUser(String username) {
		Optional<SiteUser> _siteUser = userRepository.findByUsername(username);
		
		if(_siteUser.isPresent()) {
			SiteUser siteUser = _siteUser.get();
			return siteUser;
		} else {
			throw new DataNotFoundException("해당 유저는 존재하지 않는 유저입니다.");
			
		}
	}
	
	// 유저 정보 업데이트-이메일만
	public void userUpdate(SiteUser siteUser, UserCreateForm createForm) {
		if (passwordEncoder.matches(createForm.getPassword1(), siteUser.getPassword())) {
			siteUser.setEmail(createForm.getEmail());
			userRepository.save(siteUser);
		
		} else {
			throw new DataNotFoundException("비밀번호가 틀렸습니다.");
			
		}
		
		
	}
	


}
