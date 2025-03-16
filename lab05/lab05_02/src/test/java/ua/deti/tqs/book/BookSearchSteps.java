package ua.deti.tqs.book;

import io.cucumber.java.DataTableType;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Format;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;


public class BookSearchSteps {
	Library library = new Library();
	List<Book> result = new ArrayList<>();

	@Given("I have a list of books")
	public void loadBooks(List<Book> books) {books.forEach(library::addBook);}

	@When("the customer searches for books published between {year} and {year}")
	public void setSearchParameters(final Date from, final Date to) {result = library.findBooks(from, to);}

	@Then("{int} books should have been found")
	public void verifyAmountOfBooksFound(final int booksFound) {
		assertThat(result.size(), equalTo(booksFound));
	}

	@Then("Book {int} should have the title {string}")
	public void verifyBookAtPosition(final int position, final String title) {
		assertThat(result.get(position - 1).getTitle(), equalTo(title));
	}

	@DataTableType
	public Book bookEntry(Map<String, String> entry) {
		return new Book(entry.get("title"), entry.get("author"), iso8601Date(entry.get("published")));
	}

	@ParameterType("([0-9]{4})-([0-9]{2})-([0-9]{2})")
	public Date iso8601Date(String date) {
		Integer[] split = Arrays.stream(date.split("-")).map(Integer::parseInt).toArray(Integer[]::new);
		return Date.from(LocalDate.of(split[0], split[1], split[1]).atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	@ParameterType("([0-9]{4})")
	public Date year(String year) {
		return Date.from(LocalDate.of(Integer.parseInt(year), 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
}
