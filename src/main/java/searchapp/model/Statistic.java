package searchapp.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import searchapp.entity.Site;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class Statistic {

    private Total total;
    private List<Site> detailed;
}
