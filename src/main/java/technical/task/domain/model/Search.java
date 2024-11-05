package technical.task.domain.model;

/**
 * @author Hryhorii Seniv
 * @version 2024-11-04
 */
public class Search {
    private static final int DEFAULT_LIMIT = 20;
    private static final int DEFAULT_OFFSET = 0;
    private Integer limit;
    private Integer offset;

    public Integer getLimit() {
        if (limit == null || limit == 0) {
            limit = DEFAULT_LIMIT;
        }
        return limit;
    }

    public Search setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public Integer getOffset() {
        if (offset == null || offset < 0) {
            offset = DEFAULT_OFFSET;
        }
        return offset;
    }

    public Search setOffset(Integer offset) {
        this.offset = offset;
        return this;
    }

    @Override
    public String toString() {
        return "Search{" +
                "limit=" + limit +
                ", offset=" + offset +
                '}';
    }
}
