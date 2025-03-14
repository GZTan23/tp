package seedu.address.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.student.Address;
import seedu.address.model.student.Assignment;
import seedu.address.model.student.Email;
import seedu.address.model.student.Name;
import seedu.address.model.student.Phone;
import seedu.address.model.student.Student;
import seedu.address.model.student.Subject;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Student[] getSampleStudents() {
        return new Student[] {
            new Student(new Name("Alex Yeoh"),
                    new Phone("87438807"),
                    new Email("alexyeoh@example.com"),
                    new Address("Blk 30 Geylang Street 29, #06-40"),
                    new Subject("CS2103T"),
                    getTagSet("friends"),
                    getAssignmentSet("Physics Homework 1")),
            new Student(new Name("Bernice Yu"),
                    new Phone("99272758"),
                    new Email("berniceyu@example.com"),
                    new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"),
                    new Subject("CS2100"),
                    getTagSet("colleagues", "friends"),
                    getAssignmentSet("Chinese Essay")),
            new Student(new Name("Charlotte Oliveiro"),
                    new Phone("93210283"),
                    new Email("charlotte@example.com"),
                    new Address("Blk 11 Ang Mo Kio Street 74, #11-04"),
                    new Subject("Additional Maths"),
                    getTagSet("neighbours"),
                    getAssignmentSet("Math Exercise 1")),
            new Student(new Name("David Li"),
                    new Phone("91031282"),
                    new Email("lidavid@example.com"),
                    new Address("Blk 436 Serangoon Gardens Street 26, #16-43"),
                    new Subject("Chemistry"),
                    getTagSet("family"),
                    getAssignmentSet("Chemistry Exercise Book Page 11")),
            new Student(new Name("Irfan Ibrahim"),
                    new Phone("92492021"),
                    new Email("irfan@example.com"),
                    new Address("Blk 47 Tampines Street 20, #17-35"),
                    new Subject("Sec 1 Physics"),
                    getTagSet("classmates"),
                    getAssignmentSet("English Essay")),
            new Student(new Name("Roy Balakrishnan"),
                    new Phone("92624417"),
                    new Email("royb@example.com"),
                    new Address("Blk 45 Aljunied Street 85, #11-31"),
                    new Subject("CS2103T"),
                    getTagSet("colleagues"),
                    getAssignmentSet("Math Exercise 1")),
            new Student(new Name("Zoy White"),
                    new Phone("94351253"),
                    new Email("zoyw@gnail.com"),
                    new Address("Blk 45 Aljunied Street 85, #11-31"),
                    new Subject("H2 Computing"),
                    getTagSet("colleagues"),
                    getAssignmentSet("Math Exercise 1"))
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Student sampleStudent : getSampleStudents()) {
            sampleAb.addStudent(sampleStudent);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

    public static Set<Assignment> getAssignmentSet(String... strings) {
        return Arrays.stream(strings)
                .map(Assignment::new)
                .collect(Collectors.toSet());
    }

}
