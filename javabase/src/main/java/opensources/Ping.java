package opensources;

import io.airlift.airline.Command;
import io.airlift.airline.HelpOption;
import io.airlift.airline.Option;
import io.airlift.airline.SingleCommand;

import javax.inject.Inject;

/**
 * @Author:lmq
 * @Date: 2020/10/19
 * @Desc:
 **/
@Command(name = "ping", description = "network test utility")
public class Ping {
    @Inject
    public HelpOption helpOption;

    @Option(name = {"-c", "--count"}, description = "Send count packets")
    public int count = 1;

    public static void main(String... args) {
        Ping ping = SingleCommand.singleCommand(Ping.class).parse(args);

        if (ping.helpOption.showHelpIfRequested()) {
            return;
        }

        ping.run();
    }

    public void run() {
        System.out.println("Ping count-----------------------: " + count);
    }
}
