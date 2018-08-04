package se.fredin.llama.jobengine.examples.externallinks.bean;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;
import se.fredin.llama.jobengine.bean.FxKBean;

@CsvRecord(separator = ";", generateHeaderColumns = true, skipFirstLine = true)
public class Item implements FxKBean {

    @DataField(pos = 1, columnName = "Artikelnummer", required = true)
    private String articleNumber;

    @DataField(pos = 2, columnName = "Skickad Till E-handel")
    private String sentToWeb;

    @DataField(pos = 3, columnName = "Produktbild")
    private String productImage;

    @DataField(pos = 4, columnName = "Tillagningsbild")
    private String safetySheet;

    @DataField(pos = 5, columnName = "Förpackningsbild")
    private String cookingImage;

    @DataField(pos = 6, columnName = "Säkerhetsdatablad")
    private String packagingImage;

    public String getArticleNumber() {
        return articleNumber;
    }

    public void setArticleNumber(String articleNumber) {
        this.articleNumber = articleNumber;
    }

    public String getSentToWeb() {
        return sentToWeb;
    }

    public void setSentToWeb(String sentToWeb) {
        this.sentToWeb = sentToWeb;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getSafetySheet() {
        return safetySheet;
    }

    public void setSafetySheet(String safetySheet) {
        this.safetySheet = safetySheet;
    }

    public String getCookingImage() {
        return cookingImage;
    }

    public void setCookingImage(String cookingImage) {
        this.cookingImage = cookingImage;
    }

    public String getPackagingImage() {
        return packagingImage;
    }

    public void setPackagingImage(String packagingImage) {
        this.packagingImage = packagingImage;
    }

    /**
     * @return true if {@link #getSentToWeb()} == "ja"
     */
    public boolean isSentToWeb() {
        return getSentToWeb().equalsIgnoreCase("ja");
    }

    public void clearLinks() {
        setCookingImage("");
        setPackagingImage("");
        setProductImage("");
        setSafetySheet("");
    }


    @Override
    public String getId() {
        return this.articleNumber;
    }

    @Override
    public String toString() {
        return "Item{" +
                "articleNumber='" + articleNumber + '\'' +
                ", sentToWeb='" + sentToWeb + '\'' +
                ", productImage='" + productImage + '\'' +
                ", safetySheet='" + safetySheet + '\'' +
                ", cookingImage='" + cookingImage + '\'' +
                ", packagingImage='" + packagingImage + '\'' +
                '}';
    }
}