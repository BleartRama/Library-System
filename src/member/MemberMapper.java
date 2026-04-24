package member;





    public class MemberMapper {


        public static MemberSummaryDTO toSummaryDTO(Member member) {
            return new MemberSummaryDTO(
                    member.getId(),
                    member.getFirstName(),
                    member.getLastName(),
                    member.getEmail(),
                    member.getStatus()
            );
        }

        public static MemberProfileDTO toProfileDTO(Member member) {
            return new MemberProfileDTO(
                    member.getId(),
                    member.getFirstName(),
                    member.getLastName(),
                    member.getEmail(),
                    member.getMembershipDate(),
                    member.getMembershipType(),
                    member.getStatus()
            );
        }


    }

