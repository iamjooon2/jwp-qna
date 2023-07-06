package subway;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity // (1)
@Table(name = "station") // (2)
public class Station {


    @Id // (3)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // (4)
    private Long id;

    @Column(name = "name", unique = true, nullable = false) // (5)
    private String name;

    @ManyToOne
    @JoinColumn(name = "line_id")
    private Line line;

    protected Station() { // (6)
    }

    public Station(final String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setLine(final Line line) {
        this.line = line;
        final List<Station> stations = line.getStations();
        if (!stations.contains(this)) {
            stations.add(this);
        }
    }

    public String getName() {
        return name;
    }


    public Line getLine() {
        return this.line;
    }

    public void updatName(final String name) {
        this.name = name;
    }

}
