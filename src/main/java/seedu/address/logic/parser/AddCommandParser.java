package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POSTALCODE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Date;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.PostalCode;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_DATE, PREFIX_EMAIL, PREFIX_ADDRESS,
                        PREFIX_POSTALCODE, PREFIX_TAG);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_PHONE)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        try {
            Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME)).get();
            Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE)).get();
            Date date = ParserUtil.parseDate(areValuePresent(argMultimap.getValue(PREFIX_DATE))).get();
            Email email = ParserUtil.parseEmail(areValuePresent(argMultimap.getValue(PREFIX_EMAIL))).get();
            Address address = ParserUtil.parseAddress(areValuePresent(argMultimap.getValue(PREFIX_ADDRESS))).get();
            PostalCode postalCode = getPostalCode(areValuePresent(argMultimap.getValue(PREFIX_ADDRESS)),
                    argMultimap.getValue(PREFIX_POSTALCODE));
            Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

            ReadOnlyPerson person = new Person(name, phone, date, email, address, postalCode, tagList);

            return new AddCommand(person);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
    }

    /**
     * If postal code not specified and can be found in address, return
     * postal code found in address, else return postal code entered
     * @throws IllegalValueException if postal code entered is not exactly 6 digit
     */
    private PostalCode getPostalCode(Optional<String> address, Optional<String> postalCode)
            throws IllegalValueException {
        Pattern pattern = Pattern.compile("(?<postalCode>(?<!\\d)\\d{6}(?!\\d))");
        Matcher match = pattern.matcher(address.get());

        if (!postalCode.isPresent() && match.find()) {
            return ParserUtil.parsePostalCode(Optional.ofNullable(match.group("postalCode"))).get();
        } else if (!postalCode.isPresent()) {
            return new PostalCode("");
        } else {
            return ParserUtil.parsePostalCode(postalCode).get();
        }
    }

    private static Optional<String> areValuePresent(Optional<String> checkPresent) {
        checkPresent = Optional.of(checkPresent.orElse(""));
        return checkPresent;
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
