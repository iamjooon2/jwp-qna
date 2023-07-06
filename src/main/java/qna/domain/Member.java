package qna.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany
    @JoinColumn(name = "member_id")
    private List<Favorite> favorites = new ArrayList<>();

    protected Member() {
    }

    // 실무 관점에서는 1대 다 단방향을 쓰는 경우가 있긴 하지만...
    // 보통 다대일 양방향 쓰는 것을 추천

    // JPA cascade 속성 같은 부분들은 면접때 빠싹 외워야함


}
