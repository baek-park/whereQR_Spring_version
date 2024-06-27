package whereQR.project.config;

import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;

import javax.persistence.EntityManagerFactory;
public class QueryCounter {

    private final Statistics statistics;

    public QueryCounter(EntityManagerFactory entityManagerFactory) {
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        this.statistics = sessionFactory.getStatistics();
        this.statistics.setStatisticsEnabled(true);
    }

    public long getQueryCount() {
        return statistics.getPrepareStatementCount();
    }

    public long getQueryExecutionTime() {
        return statistics.getQueryExecutionMaxTime();
    }

    public void clear() {
        statistics.clear();
    }
}