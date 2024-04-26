import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;

public class CurrencyConverter {

    private static final String API_URL = "https://v6.exchangerate-api.com/v6/3d86722418f0a71f12110061/latest/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;

        while (continuar) {
            try {
                // Obtener el código de la moneda base del usuario
                System.out.println("Seleccione la moneda base para la conversión (ingrese su clave):");
                String baseCurrencyCode = selectCurrency(scanner);

                // Realizar la solicitud HTTP para obtener los datos de la API
                String jsonResponse = getApiResponse(API_URL + baseCurrencyCode);

                // Analizar la respuesta JSON utilizando Gson
                Gson gson = new Gson();
                ExchangeRateData exchangeRateData = gson.fromJson(jsonResponse, ExchangeRateData.class);

                // Verificar si la conversión de monedas es nula
                if (exchangeRateData == null || exchangeRateData.getConversionRates() == null) {
                    System.out.println("No se pudieron obtener los datos de conversión de monedas.");
                    continue;
                }

                // Mostrar el menú de selección de monedas para la conversión
                System.out.println("Seleccione la moneda a la que desea convertir (ingrese su clave):");
                String targetCurrencyCode = selectCurrency(scanner);

                // Verificar si la moneda seleccionada está disponible en los datos de conversión
                if (exchangeRateData.getConversionRates().containsKey(targetCurrencyCode)) {
                    double conversionRate = exchangeRateData.getConversionRates().get(targetCurrencyCode);
                    System.out.println("La tasa de conversión de 1 " + baseCurrencyCode + " a " + targetCurrencyCode + " es: " + conversionRate);

                    // Realizar la conversión
                    System.out.println("Ingrese la cantidad en " + baseCurrencyCode + " que desea convertir:");
                    double amountBaseCurrency = scanner.nextDouble();
                    double amountConverted = amountBaseCurrency * conversionRate;
                    System.out.println(amountBaseCurrency + " " + baseCurrencyCode + " equivale a " + amountConverted + " " + targetCurrencyCode);
                } else {
                    System.out.println("La moneda seleccionada no está disponible para conversión.");
                }

                // Preguntar si desea continuar
                System.out.println("¿Desea realizar otra conversión? (S/N)");
                String response = scanner.next();
                if (!response.equalsIgnoreCase("S")) {
                    continuar = false;
                }

                scanner.nextLine(); // Limpiar el buffer de entrada

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        scanner.close();
    }

    private static String selectCurrency(Scanner scanner) {
        printAvailableCurrencies();
        return scanner.nextLine().toUpperCase();
    }

    private static void printAvailableCurrencies() {
        int startIndex = 0;
        int endIndex = Math.min(10, Currency.values().length);

        while (startIndex < Currency.values().length) {
            System.out.println("Mostrando resultados " + (startIndex + 1) + " de " + Currency.values().length + ":");
            // Imprimir los códigos y nombres de las monedas disponibles
            for (int i = startIndex; i < endIndex; i++) {
                Currency currency = Currency.values()[i];
                System.out.println(currency.getCode() + " - " + currency.getName());
            }

            System.out.println("-----------------------------------------");

            // Mostrar opción para ver más o menos monedas
            System.out.println("Presione 'S' para ver las siguientes 10 monedas, 'A' para ver las anteriores 10," +
                    " o ingrese cualquier carácter para continuar:");
            Scanner scanner = new Scanner(System.in);
            String response = scanner.next().toUpperCase();
            if (response.equals("S")) {
                // Mostrar las siguientes 10 monedas
                startIndex = endIndex;
                endIndex = Math.min(endIndex + 10, Currency.values().length);
            } else if (response.equals("A")) {
                // Mostrar las anteriores 10 monedas
                endIndex = startIndex;
                startIndex = Math.max(0, startIndex - 10);
            } else {
                System.out.println("Ingrese la clave de la moneda:");
                break;
            }
        }
    }

    private static String getApiResponse(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();
        return response.toString();
    }

    static class ExchangeRateData {
        private Map<String, Double> conversion_rates;

        public Map<String, Double> getConversionRates() {
            return conversion_rates;
        }
    }


    enum Currency {
        USD("Dólar estadounidense"),
        EUR("Euro"),
        GBP("Libra esterlina"),
        JPY("Yen japonés"),
        AUD("Dólar australiano"),
        CAD("Dólar canadiense"),
        CHF("Franco suizo"),
        CNY("Yuan chino"),
        HKD("Dólar de Hong Kong"),
        NZD("Dólar neozelandés"),
        SEK("Corona sueca"),
        KRW("Won surcoreano"),
        SGD("Dólar de Singapur"),
        NOK("Corona noruega"),
        MXN("Peso mexicano"),
        INR("Rupia india"),
        RUB("Rublo ruso"),
        BRL("Real brasileño"),
        ZAR("Rand sudafricano"),
        TRY("Lira turca"),
        PLN("Zloty polaco"),
        IDR("Rupia indonesia"),
        SAR("Riyal saudí"),
        AED("Dirham de los Emiratos Árabes Unidos"),
        THB("Baht tailandés"),
        MYR("Ringgit malayo"),
        PHP("Peso filipino"),
        COP("Peso colombiano"),
        DKK("Corona danesa"),
        CZK("Corona checa"),
        HUF("Forint húngaro"),
        ILS("Nuevo shekel israelí"),
        CLP("Peso chileno"),
        TWD("Dólar taiwanés"),
        ARS("Peso argentino"),
        NIO("Córdoba nicaragüense"),
        PKR("Rupia pakistaní"),
        EGP("Libra egipcia"),
        IQD("Dinar iraquí"),
        VND("Dong vietnamita"),
        MAD("Dírham marroquí"),
        DZD("Dinar argelino"),
        KES("Chelín keniano"),
        UAH("Grivna ucraniana"),
        UYU("Peso uruguayo"),
        UZS("Som uzbeko"),
        KZT("Tenge kazajo"),
        BDT("Taka bangladesí"),
        LKR("Rupia de Sri Lanka"),
        ETB("Birr etíope"),
        VEF("Bolívar venezolano"),
        CUP("Peso cubano"),
        LBP("Libra libanesa"),
        SDG("Libra sudanesa"),
        XAF("Franco CFA de África Central"),
        XOF("Franco CFA de África Occidental"),
        GHS("Cedi ghanés"),
        TND("Dinar tunecino"),
        LYD("Dinar libio"),
        JOD("Dinar jordano"),
        NAD("Dólar namibio"),
        MUR("Rupia mauriciana"),
        BHD("Dinar de Bahrein"),
        LAK("Kip laosiano"),
        MKD("Dinar macedonio"),
        MNT("Tugrik mongol"),
        PYG("Guaraní paraguayo"),
        BTN("Ngultrum de Bután"),
        XPF("Franco CFP"),
        BAM("Marco convertible de Bosnia-Herzegovina"),
        RON("Leu rumano"),
        HRK("Kuna croata"),
        SZL("Lilangeni suazi"),
        GYD("Dólar guyanés"),
        SVC("Colón salvadoreño"),
        BGN("Lev búlgaro"),
        MZN("Metical mozambiqueño"),
        ZMW("Kwacha zambiano"),
        AWG("Florín arubeño"),
        MOP("Pataca de Macao"),
        BND("Dólar de Brunei"),
        HTG("Gourde haitiano"),
        LSL("Loti lesothense"),
        XCD("Dólar del Caribe Oriental"),
        FJD("Dólar fiyiano"),
        MDL("Leu moldavo"),
        SCR("Rupia seychellense"),
        GMD("Dalasi gambiano"),
        MVR("Rupia de Maldivas"),
        TOP("Pa'anga tongano"),
        HNL("Lempira hondureño"),
        GIP("Libra de Gibraltar"),
        BWP("Pula de Botswana"),
        GTQ("Quetzal guatemalteco"),
        MRO("Ouguiya mauritano"),
        LRD("Dólar liberiano"),
        BBD("Dólar de Barbados"),
        BIF("Franco burundés"),
        DJF("Franco de Yibuti"),
        ANG("Florín antillano neerlandés"),
        KGS("Som kirguís"),
        KPW("Won norcoreano"),
        MGA("Ariary malgache"),
        RSD("Dinar serbio"),
        SOS("Chelín somalí"),
        TMT("Manat turcomano"),
        WST("Tala samoano"),
        CDF("Franco congoleño"),
        GNF("Franco guineano"),
        ZWL("Dólar zimbabuense"),
        VES("Bolívar venezolano"),
        STN("Dobra de Santo Tomé y Príncipe"),
        SRD("Dólar surinamés"),
        MRU("Ouguiya mauritano"),
        XDR("Derechos especiales de giro"),
        TVD("Dólar Tuvalu");

        private final String name;

        Currency(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public String getCode() {
            return name().toUpperCase();
        }
    }
}
