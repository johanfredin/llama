package se.fredin.llama;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.csv.CsvDataFormat;
import org.apache.camel.model.dataformat.BindyType;
import se.fredin.llama.processor.ResultType;
import se.fredin.llama.utils.Endpoint;

/**
 * Helping abstraction layer that extends the camel {@link RouteBuilder} that all
 * that is used for creating routes. Holds helper methods for retrieving spring boot
 * properties and some deault methods for getting a route up and running.
 *
 * @author johan
 */
public abstract class LlamaRoute extends RouteBuilder {

    public static int startupOrder = 1;

    public static int nextAvailableStartup() {
        return startupOrder++;
    }

    /**
     * Access a property from <b>application.properties</b> by passing in the name of the property.
     *
     * @param property the property we want the value for.
     * @return the value of the property or the name of the property surrounced by {{}} if the property is not found.
     */
    protected String prop(String property) {
        return "{{" + property + "}}";
    }

    /**
     * Calls {@link #prop(String)} with {@link Endpoint#INPUT_DIR} as parameter
     *
     * @return the default input directory property (if one specified).
     */
    protected String defaultInputDir() {
        return prop(Endpoint.INPUT_DIR);
    }

    /**
     * Calls {@link #prop(String)} with {@link Endpoint#OUTPUT_DIR} as parameter
     *
     * @return the default output directory property (if one specified).
     */
    protected String defaultOutputDir() {
        return prop(Endpoint.OUTPUT_DIR);
    }

    /**
     * Reflective call to {@link #getRoute(String, String, String, Class, String, int)}
     * passing in {@link #defaultInputDir()} as the directory parameter.
     *
     * @param routeId      the id to give the route
     * @param fileName     the name of the file we want to fetch.
     * @param clazz        the class to unmarshal the data from the file to. Needs to follow the {@link BindyType}
     *                     specification.
     * @param endpoint     the endpoint to send the the route to. Will be started with "seda:"
     * @param startupOrder the startup order of this route.
     * @return the endpoint.
     */
    protected String getRoute(String routeId, String fileName, Class clazz, String endpoint, int startupOrder) {
        return getRoute(routeId, defaultInputDir(), fileName, clazz, endpoint, startupOrder);
    }

    /**
     * When we have a file that can be used with {@link BindyType} to unmarshal into a collection
     * of beans mapped up as bindy objects and then passed further to a seda endpoint
     * to be picked up later by another route. This method does just that.
     *
     * @param routeId      the id to give the route
     * @param directory    the directory of the file we want to fetch.
     * @param fileName     the name of the file we want to fetch.
     * @param clazz        the class to unmarshal the data from the file to. Needs to follow the {@link BindyType}
     *                     specification.
     * @param endpoint     the endpoint to send the the route to. Will be started with "seda:"
     * @param startupOrder the startup order of this route.
     * @return the endpoint.
     */
    protected String getRoute(String routeId, String directory, String fileName, Class clazz, String endpoint, int startupOrder) {
        from(Endpoint.file(directory, fileName))
                .routeId(routeId)
                .unmarshal()
                .bindy(BindyType.Csv, clazz)
                .to("seda:" + endpoint)
                .startupOrder(startupOrder);

        return "seda:" + endpoint;
    }

    protected String getRoute(String routeId, String directory, String fileName, ResultType resultType, String endpoint, int startupOrder) {
        from(Endpoint.file(directory, fileName))
                .routeId(routeId)
                .unmarshal(resultType == ResultType.LIST ? csvToListOfLists() : csvToCollectionOfMaps())
                .to("seda:" + endpoint)
                .startupOrder(startupOrder);

        return "seda:" + endpoint;
    }

    /**
     * Converts the content of a .csv file into a nested list of strings.
     * Each entry in the list will be a list of strings in the order
     * the fields are written in the file.
     * Calls {@link #csvToListOfLists(char, boolean)} with default values (';', false)
     * @return a list of list of strings
     */
    protected CsvDataFormat csvToListOfLists() {
        return csvToListOfLists(';', false);
    }

    /**
     * Converts the content of a .csv file into a nested list of strings.
     * Each entry in the list will be a list of strings in the order
     * the fields are written in the file.
     * @param delimiter the delimiter used to separate entries in the csv file (default=';')
     * @param skipHeader whether or not to skip the first(header) row in the file (default=false)
     * @return a list of list of strings
     */
    protected CsvDataFormat csvToListOfLists(char delimiter, boolean skipHeader) {
        var format = new CsvDataFormat();
        format.setDelimiter(delimiter);
        format.setSkipHeaderRecord(skipHeader);
        return format;
    }

    /**
     * Calls {@link #csvToCollectionOfMaps(char, boolean)} with default values <b>';', true</b>
     * Converts the content of a .csv file into a list of maps where key/values are all strings.
     * Each entry in the list will be a map where <b>key=header name, value=value at header index.</b>.
     * @return the content of a .csv file as a list of ordered maps.
     */
    protected CsvDataFormat csvToCollectionOfMaps() {
        return csvToCollectionOfMaps(';', true);
    }

    /**
     * Calls {@link #csvToCollectionOfMaps(char, boolean)} with passed in delimiter and default value true for ordered maps
     * Converts the content of a .csv file into a list of maps where key/values are all strings.
     * Each entry in the list will be a map where <b>key=header name, value=value at header index.</b>.
     * @param delimiter the delimiter used to separate the fields in the .csv file (default is ';')
     * @return the content of a .csv file as a list of ordered maps.
     */
    protected CsvDataFormat csvToCollectionOfMaps(char delimiter) {
        return csvToCollectionOfMaps(delimiter, true);
    }

    /**
     * Converts the content of a .csv file into a list of maps where key/values are all strings.
     * Each entry in the list will be a map where <b>key=header name, value=value at header index.</b>.
     * Here we have the option to disable ordered maps and use standard maps. This could potentially
     * be useful when we have a really big amount of data or we simply don't care if the values
     * are ordered or not.
     * @param delimiter the delimiter used to separate the fields in the .csv file (default is ';')
     * @param isOrderedMaps whether to use ordered maps or standard maps (default is true)
     * @return the content of a .csv file as a list of ordered (or standard) maps.
     */
    protected CsvDataFormat csvToCollectionOfMaps(char delimiter, boolean isOrderedMaps) {
        var format = new CsvDataFormat();
        format.setDelimiter(delimiter);
        if(isOrderedMaps) {
            format.setUseOrderedMaps(true);
        } else {
            format.setUseMaps(true);
        }
        return format;
    }
}


