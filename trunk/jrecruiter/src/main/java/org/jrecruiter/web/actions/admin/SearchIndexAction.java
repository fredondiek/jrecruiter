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
package org.jrecruiter.web.actions.admin;

import org.apache.struts2.convention.annotation.Result;
import org.jrecruiter.web.actions.BaseAction;
import org.jrecruiter.web.interceptor.RetrieveMessages;

/**
 * Re-index the Hibernate Search Index
 *
 * @author Gunnar Hillert
 * @version $Id$
 *
 */
@Result(name="success", location="index", type="redirectAction")
public class SearchIndexAction extends BaseAction {

    /** serialVersionUID. */
    private static final long serialVersionUID = 4614516114027504626L;

    /**
     *
     */
    @Override
    @RetrieveMessages
    public String execute() {
            jobService.reindexSearch();
            super.addActionMessage("Re-indexing of the search index triggered.");
            return SUCCESS;
    }
}
