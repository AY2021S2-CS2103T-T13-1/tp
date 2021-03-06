package seedu.weeblingo.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.logging.Logger;

import seedu.weeblingo.commons.core.LogsCenter;
import seedu.weeblingo.commons.core.Messages;
import seedu.weeblingo.logic.commands.exceptions.CommandException;
import seedu.weeblingo.model.Mode;
import seedu.weeblingo.model.Model;

/**
 * Gets the next flashcard in the quiz session.
 */
public class NextCommand extends Command {

    public static final String COMMAND_WORD = "next";

    public static final String MESSAGE_SUCCESS = "Here is the next question.\n"
            + "Enter \"check\" to check the answer, "
            + "\"next\" to move to the next question or \"end\" to return to menu.";

    public static final String MESSAGE_QUIZ_ENDED = "The Quiz is over! Your score has been recorded:\n";

    public static final String MESSAGE_CORRECT_ATTEMPTS_HELPER = "You answered the following question(s) correctly: \n";

    public static final String MESSAGE_QUIZ_ALREADY_ENDED = "The quiz session has already ended. \n";

    public static final String MESSAGE_QUIZ_END_ACTIONS = "Please enter \"quiz\" to return to quiz view, "
            + "\"start\" to start a new session, or \"end\" to return to menu.\n";

    private static final Logger logger = LogsCenter.getLogger(CheckCommand.class);

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        int currentMode = model.getCurrentMode();

        if (currentMode == Mode.MODE_QUIZ_SESSION_ENDED) {
            throw new CommandException(MESSAGE_QUIZ_ALREADY_ENDED + MESSAGE_QUIZ_END_ACTIONS);
        }

        if (currentMode != Mode.MODE_QUIZ_SESSION && currentMode != Mode.MODE_CHECK_SUCCESS) {
            throw new CommandException(Messages.MESSAGE_NOT_IN_QUIZ_SESSION);
        }

        requireNonNull(model.getQuizInstance());
        logger.info("Moving on to the next question in session");

        if (model.getNextFlashcard() == null) {
            String quizStatistics = model.getQuizStatisticString() + "\n";
            model.addScore();
            model.showAttemptedQuestions();
            String correctAttempts = model.getCorrectAttemptsIndexes().toString() + "\n";
            model.switchModeQuizSessionEnded();
            return new CommandResult(MESSAGE_QUIZ_ENDED
                    + quizStatistics + MESSAGE_CORRECT_ATTEMPTS_HELPER + correctAttempts);
        }

        model.switchModeQuizSession();
        return new CommandResult(MESSAGE_SUCCESS, false, false);
    }
}
