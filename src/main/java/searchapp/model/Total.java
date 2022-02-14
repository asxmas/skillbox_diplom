package searchapp.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Total {

    private long sites;
    private long pages;
    private long lemmas;
    private boolean isIndexing;
}
