# Review questions:
## a) Identify a couple of examples that use AssertJ expressive methods chaining.

Some examples of AssertJ expressive methods chaining are shown below:

```java
@Test
void whenCreateAlex_thenReturnAlexEmployee() {

    // arrange a new employee and insert into db
    Employee persistedAlex = entityManager.persistFlushFind( new Employee("alex", "alex@deti.com")); //ensure data is persisted at this point

    // test the query method of interest
    Employee found = employeeRepository.findByName("alex");
    assertThat(found).isNotNull().
            extracting(Employee::getName).isEqualTo(persistedAlex.getName());
}
```

```java
@Test
void givenSetOfEmployees_whenFindAll_thenReturnAllEmployees() {
    Employee alex = new Employee("alex", "alex@deti.com");
    Employee ron = new Employee("ron", "ron@deti.com");
    Employee bob = new Employee("bob", "bob@deti.com");

    entityManager.persist(alex);
    entityManager.persist(bob);
    entityManager.persist(ron);
    entityManager.flush();

    List<Employee> allEmployees = employeeRepository.findAll();
    assertThat(allEmployees).hasSize(3).extracting(Employee::getName).containsOnly(alex.getName(), ron.getName(), bob.getName());
}
```

## b) Take note of transitive annotations included in @DataJpaTest.

The @DataJpaTest annotation is a composed annotation that provides a convenient way to include the following annotations:

- `@ExtendWith(SpringExtension.class)`: Integrates the Spring TestContext Framework into JUnit. It allows the Spring testing context to be properly set up for each test.

`@AutoConfigureTestDatabase`: Used to configure the database for the tests. By default, it uses in-memory database but can be customized for other databases.

- `@Transactional`: Assures that each test method is run within a transaction. After each test, the transaction is rolled back.

- `@ContextConfiguration`: Load the application context for the test.

- `@TestConfiguration`: Allow additional configurations or beans for the test context, separate from the main application configuration.

## c) Identify an example in which you mock the behavior of the repository (and avoid involving a database).

By using the `Mockito.when().thenReturn()` method, we can mock the behavior of the repository and avoid involving a database. This is shown in the example below:

```java
@BeforeEach
public void setUp() {

    //these expectations provide an alternative to the use of the repository
    Employee john = new Employee("john", "john@deti.com");
    john.setId(111L);

    Employee bob = new Employee("bob", "bob@deti.com");
    Employee alex = new Employee("alex", "alex@deti.com");

    List<Employee> allEmployees = Arrays.asList(john, bob, alex);

    Mockito.when(employeeRepository.findByName(john.getName())).thenReturn(john);
    Mockito.when(employeeRepository.findByName(alex.getName())).thenReturn(alex);
    Mockito.when(employeeRepository.findByName("wrong_name")).thenReturn(null);
    Mockito.when(employeeRepository.findById(john.getId())).thenReturn(Optional.of(john));
    Mockito.when(employeeRepository.findAll()).thenReturn(allEmployees);
    Mockito.when(employeeRepository.findById(-99L)).thenReturn(Optional.empty());
}
```


## d) What is the difference between standard @Mock and @MockBean?

Mockito provides `@Mock`, with this we can create a mock object of a class or an interface. This is used to mock the behavior of a class or an interface.
However, Mockito has no knowledge about Spring Beans, so it cannot inject the mock object into the Spring context. 
This is where `@MockBean` comes in. `@MockBean` is used to add mock objects to the Spring application context.
This is useful when we want to mock the behavior of a Spring Bean in a Spring Boot application.

## e) What is the role of the file “application-integrationtest.properties”? In which conditions will it be used?

In this file we can define the properties that will be used during integration tests.
On unit tests, a mock is used (to test the business logic) as such we do not need to have real connections.
However, on integration tests, we need to have real connections, so we need to define the properties that will be used during these tests.

## f) the sample project demonstrates three test strategies to assess an API (C, D and E) developed with SpringBoot. Which are the main/key differences? 

- C mocks the Service and so it doesn't perform any database query, since answers are mocked,
- D uses MockMvc, which mocks the Model-View-Controller layout,
- E uses a RestTemplate, which acts as a REST API crawler. 

D and E contain integration tests and thus use a real database connection. 
The first one parses the response directly, while the second one gets the response, and then uses AssertJ asserts on it.

