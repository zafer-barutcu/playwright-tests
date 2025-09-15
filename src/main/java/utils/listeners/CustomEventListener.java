package utils.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;

public class CustomEventListener implements ConcurrentEventListener {
    private static final Logger logger = LoggerFactory.getLogger(CustomEventListener.class);

    private String getCurrentTime() {
        return java.time.LocalDateTime.now().toString();
    }

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestStepFinished.class, this::handleTestStepFinished);
    }

    private void handleTestStepFinished(TestStepFinished event) {
        TestStep testStep = event.getTestStep();

        if (testStep instanceof io.cucumber.plugin.event.PickleStepTestStep) {
            io.cucumber.plugin.event.PickleStepTestStep step =
                    (io.cucumber.plugin.event.PickleStepTestStep) testStep;

            Status status = event.getResult().getStatus();

            // Bu, log formatına uygun şekilde çıktıyı yazdıracaktır.
            logger.info("--> {} - {}", step.getStep().getText(), status);
        }
    }
}


