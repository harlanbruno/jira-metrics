package com.devfuncionar.jirametrics;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.collections4.comparators.ComparableComparator;
import org.apache.commons.lang.NumberUtils;
import org.apache.commons.lang.StringUtils;

import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.IssueRestClient.Expandos;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.SearchRestClient;
import com.atlassian.jira.rest.client.api.domain.BasicVotes;
import com.atlassian.jira.rest.client.api.domain.ChangelogGroup;
import com.atlassian.jira.rest.client.api.domain.ChangelogItem;
import com.atlassian.jira.rest.client.api.domain.Comment;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueType;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.util.concurrent.Promise;
import com.devfuncionar.jirametrics.business.CSVWriter;
import com.devfuncionar.jirametrics.domain.Status;
import com.devfuncionar.jirametrics.domain.StatusEn;


/**
 * http://www.purusothaman.me/getting-all-jira-issue-details-using-java/
 * http://www.purusothaman.me/getting-jira-issues-based-on-filter-using-java/
 * https://www.baeldung.com/jira-rest-api
 * https://community.atlassian.com/t5/Answers-Developer-Questions/Issue-history-through-REST-API/qaq-p/503830
 * https://docs.atlassian.com/software/jira/docs/api/REST/6.2.7/
 * https://bitbucket.org/atlassian/atlassian-connect-spring-boot/branch/feature/ACSPRING-20-jira-rest-java-client#diff
 * 
 * @author Zupper
 *
 */
// @SpringBootApplication
public class JiraApplication {

	private String username;
	private String password;
	private String jiraUrl;
	private static JiraRestClient restClient;

	private JiraApplication(String username, String password, String jiraUrl) {
		this.username = username;
		this.password = password;
		this.jiraUrl = jiraUrl;
		restClient = getJiraRestClient();
	}

	public static void main(String[] args) throws IOException {
		// SpringApplication.run(BalanceteApplication.class, args);

		// timer
		long startTime = System.nanoTime();
		
		JiraApplication myJiraClient = new JiraApplication("xb204243", "Onvf!0404", "http://jira.produbanbr.corp");

		String project = "PPJWPBR";
		
		String jql = "project = " + project + " AND issuetype in standardIssueTypes() AND issueType != Epic"; // AND id = PPJWPBR-881";
		int maxPerQuery = 100;
		int startIndex = 0;
		List<com.devfuncionar.jirametrics.domain.Issue> issues = new ArrayList<>();

		try {
			SearchRestClient searchRestClient = restClient.getSearchClient();
			IssueRestClient issueRestClient = restClient.getIssueClient();
			
			while (true) {
				
				Promise<SearchResult> searchResult = searchRestClient.searchJql(jql, maxPerQuery, startIndex, null);
				SearchResult results = searchResult.claim();

				Expandos[] expandArr = new Expandos[] { Expandos.CHANGELOG };
				List<Expandos> expand = Arrays.asList(expandArr);

				for (Issue issue : results.getIssues()) {
					
					final Issue _issue = issueRestClient.getIssue(issue.getKey(), expand).claim();
					IssueType type = _issue.getIssueType();

//					if (!type.isSubtask()) {
						
						// TODO: all
						com.devfuncionar.jirametrics.domain.Issue newIssue = new com.devfuncionar.jirametrics.domain.Issue();
						newIssue.setId(_issue.getKey());
						newIssue.setCreated(_issue.getCreationDate().toDate());
						newIssue.setDescription(_issue.getSummary());
						newIssue.setType(_issue.getIssueType().getName());
//						newIssue.addStatus(new Status(_issue.getCreationDate().toDate(), StatusEn.TEAM_BACKLOG));
						
						String team = project;
						
//						if(_issue.getSummary().indexOf("[") != -1 && _issue.getSummary().indexOf("]") != -1) {
//							team = _issue.getSummary().substring(_issue.getSummary().indexOf("[")+1, _issue.getSummary().indexOf("]"));
//						}
						
						if(_issue.getSummary().startsWith("[")) {
							team = StringUtils.substringBetween(_issue.getSummary(), "[", "]");
						}
						
						newIssue.setTeam(team);
						
						System.out.println("----------------------------");
//						System.out.println(_issue.getKey() + " - Type: " + type.getName());
						System.out.println(issue.getKey() + " - Type: " + issue.getIssueType().getName());
						System.out.println("----------------------------");

						if (_issue.getChangelog() != null) {
							
							Stack<Status> status = new Stack<>();
							status.push(new Status(_issue.getCreationDate().toDate(), StatusEn.TEAM_BACKLOG));

							for (ChangelogGroup changelogGroup : _issue.getChangelog()) {
								for (ChangelogItem changelogItem : changelogGroup.getItems()) {
									if (StringUtils.equalsIgnoreCase(changelogItem.getField(), "status")) {

										LocalDateTime date = new java.sql.Timestamp(
												changelogGroup.getCreated().getMillis()).toLocalDateTime();
										DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
										String formatDateTime = date.format(formatter);
										System.out.println("Date: " + formatDateTime + " from '"
										// + changelogItem.getField() + "' of type '"
										// + changelogItem.getFieldType() + "' from value '"
										// + changelogItem.getFrom() + "' and string '"
												+ changelogItem.getFromString() + "' to '"
												// + changelogItem.getTo() + "' and string '"
												+ changelogItem.getToString() + "'");
										
										Status _status = new Status(changelogGroup.getCreated().toDate(), StatusEn.getByDescription(changelogItem.getToString().toUpperCase()));
										
										if (_status.getStatus() != null && status.peek().getStatus().getStep() < _status.getStatus().getStep()) {
											status.push(_status);
										}
									}
								}
							}
							newIssue.addStatus(status);
						}
						
						issues.add(newIssue);
						System.out.println("----------------------------");
//					}
				}

				if (startIndex >= results.getTotal()) {
					break;
				}

				startIndex += maxPerQuery;

				System.out.println("Fetching from Index: " + startIndex);
				long endTime = System.nanoTime();
				
				long duration = (endTime - startTime);
				
				System.out.println("Search Time: " + TimeUnit.NANOSECONDS.toSeconds(duration) + "s");
			}

		} finally {
			restClient.close();
		}
		
        
		try {
			
			CSVWriter writer = new CSVWriter();
			writer.write(issues);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String createIssue(String projectKey, Long issueType, String issueSummary) {

		IssueRestClient issueClient = restClient.getIssueClient();
		IssueInput newIssue = new IssueInputBuilder(projectKey, issueType, issueSummary).build();

		return issueClient.createIssue(newIssue).claim().getKey();
	}

	private Issue getIssue(String issueKey) {
		return restClient.getIssueClient().getIssue(issueKey).claim();
	}

	private void voteForAnIssue(Issue issue) {
		restClient.getIssueClient().vote(issue.getVotesUri()).claim();
	}

	private int getTotalVotesCount(String issueKey) {
		BasicVotes votes = getIssue(issueKey).getVotes();
		return votes == null ? 0 : votes.getVotes();
	}

	private void addComment(Issue issue, String commentBody) {
		restClient.getIssueClient().addComment(issue.getCommentsUri(), Comment.valueOf(commentBody));
	}

	private List<Comment> getAllComments(String issueKey) {
		return StreamSupport.stream(getIssue(issueKey).getComments().spliterator(), false).collect(Collectors.toList());
	}

	private void updateIssueDescription(String issueKey, String newDescription) {
		IssueInput input = new IssueInputBuilder().setDescription(newDescription).build();
		restClient.getIssueClient().updateIssue(issueKey, input).claim();
	}

	private void deleteIssue(String issueKey, boolean deleteSubtasks) {
		restClient.getIssueClient().deleteIssue(issueKey, deleteSubtasks).claim();
	}

	private JiraRestClient getJiraRestClient() {
		return new AsynchronousJiraRestClientFactory().createWithBasicHttpAuthentication(getJiraUri(), this.username,
				this.password);
	}

	private URI getJiraUri() {
		return URI.create(this.jiraUrl);
	}
}
