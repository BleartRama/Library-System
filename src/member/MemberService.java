package member;


import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class MemberService {

    private final MemberRepository memberRepository = new MemberRepository();

    public List<MemberSummaryDTO> getAllMembers() throws SQLException {
        List<Member> members = memberRepository.findAll();
        return members.stream()
                .map(MemberMapper::toSummaryDTO)
                .toList();
    }

    public MemberProfileDTO getMemberProfile(int id) throws SQLException {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(id));
        return MemberMapper.toProfileDTO(member);
    }

    public void createMember(String firstName, String lastName, String email, String password, String membershipType) throws SQLException {
        Member member = new Member(
                firstName,
                lastName,
                email,
                password,
                LocalDate.now(),
                membershipType,
                "ACTIVE");
        memberRepository.save(member);
    }
}
