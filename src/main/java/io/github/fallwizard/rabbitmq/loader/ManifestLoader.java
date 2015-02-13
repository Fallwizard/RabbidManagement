package io.github.fallwizard.rabbitmq.loader;

import com.google.common.base.Preconditions;
import com.google.common.io.Files;

import java.io.File;

/**
 * @author Richard Clayton (Berico Technologies)
 */
public class ManifestLoader {

    public static void main(String[] args) throws Exception {

        if (args[0].equals("--help")){

            printHelp();

            return;
        }

        File fileToLoad = new File(args[0]);

        Preconditions.checkNotNull(args[0], "Please indicate the file to load as the first argument.");

        Preconditions.checkArgument(fileToLoad.exists(),
                String.format("File '%s' does not exist.", args[0]));

        Preconditions.checkArgument(fileToLoad.canRead(),
                String.format("File '%s' cannot be read.  Please check permissions.", args[0]));

        String extension = Files.getFileExtension(fileToLoad.getName());

        Preconditions.checkArgument(ManifestPersister.hasSerializerFor(extension),
                String.format("No support for reading files with extension '%s'.", extension));

        Manifest manifest = ManifestPersister.loadFromFile(fileToLoad);

        if (args.length == 2 && args[1] != null){

            if (args[1].equals("--rollback")){

                ManifestProvisioner.rollback(manifest);

                System.out.println("Finished rollback.");
            }
            else {

                System.out.println(
                        String.format("Invalid parameter '%s'. Use --help to get a list of available options.", args[1]));
            }
        }
        else {

            ManifestProvisioner.provision(manifest);

            System.out.println("Finished provisioning.");
        }
    }

    private static void printHelp() {

        System.out.println("Rabbid Management v3.4.0");
        System.out.println("");
        System.out.println("Manifest Loader - provisions or rollback RabbitMQ topology from an external manifest file.");
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println("Usage: {loader-cmd} --help");
        System.out.println("       {loader-cmd} [manifest location]");
        System.out.println("       {loader-cmd} [manifest location] --rollback");
        System.out.println("");
        System.out.println("Parameters:");
        System.out.println("\t--help = this message.");
        System.out.println("\t[manifest location] = the path to the manifest file.");
        System.out.println("\t--rollback = (optional) removes all objects specified in the manifest.");
        System.out.println("");
    }


}
