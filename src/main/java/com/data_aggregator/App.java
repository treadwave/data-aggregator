package com.data_aggregator;
import com.data_aggregator.cli.Automatic;
import com.data_aggregator.cli.Interactive;


public class App {
    public static void main(String[] args) {
        try {
            if (args.length > 0) {
                new Automatic().run(args);
            } else {
                new Interactive().run();
            }
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }
}


/*
cd /Users/albert/vscode/java/data-aggregator ; /usr/bin/env /Users/albert/.gradle/jdks/eclipse_adoptium-21-aarch64-os_x.2/jdk-21.0.5+11/Contents/Home/bin/java @/var/folders/1c/2dymcgtj3ml_mtbkdljt767w0000gn/T/cp_h5pjo6949dd8mlfhn0xupg47.argfile com.data_aggregator.App java -cp "target/classes:$(cat target/classpath.txt)" com.data_aggregator.App \
  --apis=simkl,weatherstack \
  --format=json \
  --append=false \
  --polling=true \
  --threads=2 \
  --interval=60
*/
