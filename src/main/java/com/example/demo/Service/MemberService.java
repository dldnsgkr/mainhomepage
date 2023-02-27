package com.example.demo.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.springframework.stereotype.Service;

import com.example.demo.DTO.BoardDTO;
import com.example.demo.DTO.MemberDTO;
import com.example.demo.domain.entity.Board;
import com.example.demo.domain.entity.Member;
import com.example.demo.domain.repository.MemberRepository;

import jakarta.transaction.Transactional;


@Service
public class MemberService {

	private MemberRepository memberRepository;
	
	public MemberService(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}
	
	
	@Transactional
	public MemberDTO login(String memid, String mempw) {
		Optional<Member> memberwrapper = memberRepository.login(memid, mempw);
		if(memberwrapper.isPresent()) {
		Member member = memberwrapper.get();
		MemberDTO memberDTO = MemberDTO.builder()
				.id(member.getId())
				.memid(member.getMemid())
				.mempw(member.getMempw())
				.name(member.getName())
				.age(member.getAge())
				.address(member.getAddress())
				.e(member.getE())
				.mail(member.getMail())
				.build();
		return memberDTO;
		} else {
			MemberDTO MemberDTO = null;
			return MemberDTO;
		}
		
	}

	@Transactional
	public void saveMember(MemberDTO memberDTO) {
		memberRepository.save(memberDTO.toEntity()).getId();
	}


	public int checkMemid(String memid) {
		int members = memberRepository.checkmemid(memid);
		//List<MemberDTO> memberDTOList = new ArrayList<>();
		//if(members.isEmpty()) return memberDTOList;
		//for(Member member : members) {
			//memberDTOList.add(this.convertEntityToDto(member));
		//}
		return members;
		
		}


	public void updateage() {
		memberRepository.updateage();
		
	}

		
		/*private MemberDTO convertEntityToDto(Member member) {
			return MemberDTO.builder()
					.id(member.getId())
					.memid(member.getMemid())
					.mempw(member.getMempw())
					.name(member.getName())
					.build();
		}*/
	
}
