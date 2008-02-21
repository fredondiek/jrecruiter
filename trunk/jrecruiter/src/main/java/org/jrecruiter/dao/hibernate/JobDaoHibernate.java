/*
 *	http://www.jrecruiter.org
 *
 *	Disclaimer of Warranty.
 *
 *	Unless required by applicable law or agreed to in writing, Licensor provides
 *	the Work (and each Contributor provides its Contributions) on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied,
 *	including, without limitation, any warranties or conditions of TITLE,
 *	NON-INFRINGEMENT, MERCHANTABILITY, or FITNESS FOR A PARTICULAR PURPOSE. You are
 *	solely responsible for determining the appropriateness of using or
 *	redistributing the Work and assume any risks associated with Your exercise of
 *	permissions under this License.
 *
 */
package org.jrecruiter.dao.hibernate;

import java.text.ParseException;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.jrecruiter.Constants.StatsMode;
import org.jrecruiter.dao.JobDao;
import org.jrecruiter.model.Job;
import org.springframework.stereotype.Repository;

/**
 * This DAO provides job-related database methods.
 *
 * @author Jerzy Puchala, Gunnar Hillert
 * @version $Id$
 */
@Repository
public final class JobDaoHibernate extends GenericDaoHibernate< Job, Long>
implements JobDao {

    /**
     * Constructor.
     *
     */
    private JobDaoHibernate() {
        super(Job.class);
    }

    /**
     * Method for returning list of all jobs.
     *
     * @return List of Jobs
     *
     */
    @SuppressWarnings("unchecked")
    public List < Job > getAllJobs() {

        List < Job > jobs = sf.getCurrentSession()
        .createQuery("select job from Job job "
                + "left outer join fetch job.statistic "
                + " order by job.updateDate DESC")
                .list();

        return jobs;
    }

    /**
     * Method for getting users jobs.
     *
     * @param username name of user owning the job.
     * @return List of Job objects for given User
     * @see org.jrecruiter.persistent.dao.
     *      JobReqDAO#getAllUserJobs(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public List < Job > getAllUserJobs(final String username) {

        List < Job > jobs = sf.getCurrentSession()
        .createQuery("from Job j where j.user.username=:username")
        .setString("username", username)
        .list();
        return jobs;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jrecruiter.persistent.dao.
     *      JobReqDAO#getAllUserJobs(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public List < Job > getAllUserJobsForStatistics(Long userId) {

        List < Job > jobs = sf.getCurrentSession()
        .createQuery("from Job j left outer join fetch j.statistic where j.user.id=:userId")
        .setLong("userId", userId)
        .list();
        return jobs;
    }

    /**
     * Method for returning list of jobs owned by the user for statistical
     * purposes.
     *
     * @param username username for which statistics shall be obtained
     * @param maxResult maximum number of statistics objects returned
     * @param statsMode  what type of statistical information to be generated
     * @return List of jobs.
     *
     * @see org.jrecruiter.dao.JobsDao#getUsersJobsForStatistics(java.lang.String,
     *      java.lang.Integer, org.jrecruiter.Constants.StatsMode)
     */
    @SuppressWarnings("unchecked")
    public List < Job > getUsersJobsForStatistics(final Long userId,
            final Integer maxResult,
            final StatsMode statsMode,
            final Boolean administrator) {

        final List < Job > jobs;

            Query query = null;

            if (statsMode == StatsMode.PAGE_HITS) {

                if (administrator) {

                    query = sf.getCurrentSession()
                    .createQuery("select j from Job j left outer join fetch j.statistic as stats "
                            + "where stats is not null order by stats.counter desc");

                } else {

                    query = sf.getCurrentSession()
                    .createQuery("select j from Job j left outer join fetch j.statistic as stats "
                            + "where j.user.id=:userId and stats is not null "
                            + "order by stats.counter desc");
                    query.setLong("userId", userId);
                }
            } else {

                if (administrator) {
                    query = sf.getCurrentSession()
                    .createQuery("select j from Job j left outer join fetch j.statistic as stats "
                            + "where stats is not null order by stats.uniqueVisits desc");
                } else {

                    query = sf.getCurrentSession()
                    .createQuery("select j from Job j left outer join fetch j.statistic as stats "
                            + "where j.user.id=:userId and stats is not null "
                            + "order by stats.uniqueVisits desc");
                    query.setLong("userId", userId);
                }
            }

            query.setMaxResults(maxResult);

            jobs = query.list();

            return jobs;
        }

        /**
         * Perform a simple search within the persistence store.
         *
         * @param keyword
         *            The search keyword
         * @return List of job postings representing the search results.
         */
        @SuppressWarnings("unchecked")
        public List<Job> searchByKeyword(final String keyword) {

    		FullTextSession fullTextSession = Search.createFullTextSession(sf.getCurrentSession());

    		MultiFieldQueryParser parser = new MultiFieldQueryParser( new String[]{"summary", "body"},
    		  new StandardAnalyzer());
    		try {
    		org.apache.lucene.search.Query query = parser.parse(keyword);

    		org.hibernate.Query hibQuery = fullTextSession.createFullTextQuery( query, Job.class );
    		List<Job> result = hibQuery.list();

            return result;
      		} catch ( org.apache.lucene.queryParser.ParseException e) {
    			throw new IllegalStateException(e);
    		}
        }

        /**
         * Method for returning list of available job postings.
         * @param pageSize Max number of results returned
         * @param pageNumber Which page are you one?
         * @param fieldSorted Which field shall be sorted
         * @param sortOrder What is the sort order?
         * @return List of jobs.
         */
        @SuppressWarnings("unchecked")
        public List < Job > getJobs(
                final Integer pageSize,
                final Integer pageNumber,
                String fieldSorted,
                String sortOrder) {
            List < Job > jobs;

            if (fieldSorted == null || fieldSorted.length() == 0) {
                fieldSorted = "updateDate";
            }

            if (sortOrder == null) {
                sortOrder = "DESC";
            } else if (!sortOrder.equalsIgnoreCase("ASC")
                    && !sortOrder.equalsIgnoreCase("DESC")) {
                sortOrder = "DESC";
            }

            final Criteria criteria = sf.getCurrentSession().createCriteria(Job.class);
            criteria.setFetchMode("statistics", FetchMode.JOIN);

            if (sortOrder.equalsIgnoreCase("DESC")) {
                criteria.addOrder(Order.desc(fieldSorted));
            } else if (sortOrder.equalsIgnoreCase("ASC")) {
                criteria.addOrder(Order.asc(fieldSorted));
            }

            criteria.setFirstResult((pageNumber - 1) * pageSize);
            criteria.setMaxResults(pageSize);

            jobs = criteria.list();

            return jobs;
        }

        /**
         * Returns the number of totally available jobs in the system.
         *
         * @return Total number of jobs
         * @see org.jrecruiter.dao.JobsDao#getJobsCount()
         */
        public Integer getJobsCount() {

            Long numberOfJobs = null;

            Query query = sf.getCurrentSession().createQuery("select count(*) from Job");
            numberOfJobs = (Long) query.uniqueResult();

            //FIXME
            return Integer.valueOf(numberOfJobs.toString());
        }
    }
