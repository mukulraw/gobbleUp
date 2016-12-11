package com.gobble.gobble_up;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class updateNotificationBean {

    @SerializedName("package_name")
    @Expose
    private String packageName;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("rating")
    @Expose
    private Double rating;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("cat_int")
    @Expose
    private Integer catInt;
    @SerializedName("cat_type")
    @Expose
    private Integer catType;
    @SerializedName("cat_key")
    @Expose
    private String catKey;
    @SerializedName("cat_keys")
    @Expose
    private List<String> catKeys = null;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("price_numeric")
    @Expose
    private Integer priceNumeric;
    @SerializedName("iap")
    @Expose
    private Boolean iap;
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("content_rating")
    @Expose
    private String contentRating;
    @SerializedName("market_update")
    @Expose
    private String marketUpdate;
    @SerializedName("screenshots")
    @Expose
    private List<String> screenshots = null;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("lang")
    @Expose
    private String lang;
    @SerializedName("from_developer")
    @Expose
    private List<Object> fromDeveloper = null;
    @SerializedName("i18n_lang")
    @Expose
    private List<String> i18nLang = null;
    @SerializedName("price_i18n_countries")
    @Expose
    private List<Object> priceI18nCountries = null;
    @SerializedName("short_desc")
    @Expose
    private String shortDesc;
    @SerializedName("downloads")
    @Expose
    private String downloads;
    @SerializedName("downloads_min")
    @Expose
    private Integer downloadsMin;
    @SerializedName("downloads_max")
    @Expose
    private Integer downloadsMax;
    @SerializedName("developer")
    @Expose
    private String developer;
    @SerializedName("number_ratings")
    @Expose
    private Integer numberRatings;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("icon_72")
    @Expose
    private String icon72;
    @SerializedName("website")
    @Expose
    private String website;
    @SerializedName("promo_video")
    @Expose
    private String promoVideo;
    @SerializedName("market_url")
    @Expose
    private String marketUrl;
    @SerializedName("deep_link")
    @Expose
    private String deepLink;

    /**
     *
     * @return
     * The packageName
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     *
     * @param packageName
     * The package_name
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     *
     * @return
     * The title
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     * The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     * The description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     * The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     * The rating
     */
    public Double getRating() {
        return rating;
    }

    /**
     *
     * @param rating
     * The rating
     */
    public void setRating(Double rating) {
        this.rating = rating;
    }

    /**
     *
     * @return
     * The category
     */
    public String getCategory() {
        return category;
    }

    /**
     *
     * @param category
     * The category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     *
     * @return
     * The catInt
     */
    public Integer getCatInt() {
        return catInt;
    }

    /**
     *
     * @param catInt
     * The cat_int
     */
    public void setCatInt(Integer catInt) {
        this.catInt = catInt;
    }

    /**
     *
     * @return
     * The catType
     */
    public Integer getCatType() {
        return catType;
    }

    /**
     *
     * @param catType
     * The cat_type
     */
    public void setCatType(Integer catType) {
        this.catType = catType;
    }

    /**
     *
     * @return
     * The catKey
     */
    public String getCatKey() {
        return catKey;
    }

    /**
     *
     * @param catKey
     * The cat_key
     */
    public void setCatKey(String catKey) {
        this.catKey = catKey;
    }

    /**
     *
     * @return
     * The catKeys
     */
    public List<String> getCatKeys() {
        return catKeys;
    }

    /**
     *
     * @param catKeys
     * The cat_keys
     */
    public void setCatKeys(List<String> catKeys) {
        this.catKeys = catKeys;
    }

    /**
     *
     * @return
     * The price
     */
    public String getPrice() {
        return price;
    }

    /**
     *
     * @param price
     * The price
     */
    public void setPrice(String price) {
        this.price = price;
    }

    /**
     *
     * @return
     * The priceNumeric
     */
    public Integer getPriceNumeric() {
        return priceNumeric;
    }

    /**
     *
     * @param priceNumeric
     * The price_numeric
     */
    public void setPriceNumeric(Integer priceNumeric) {
        this.priceNumeric = priceNumeric;
    }

    /**
     *
     * @return
     * The iap
     */
    public Boolean getIap() {
        return iap;
    }

    /**
     *
     * @param iap
     * The iap
     */
    public void setIap(Boolean iap) {
        this.iap = iap;
    }

    /**
     *
     * @return
     * The version
     */
    public String getVersion() {
        return version;
    }

    /**
     *
     * @param version
     * The version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     *
     * @return
     * The contentRating
     */
    public String getContentRating() {
        return contentRating;
    }

    /**
     *
     * @param contentRating
     * The content_rating
     */
    public void setContentRating(String contentRating) {
        this.contentRating = contentRating;
    }

    /**
     *
     * @return
     * The marketUpdate
     */
    public String getMarketUpdate() {
        return marketUpdate;
    }

    /**
     *
     * @param marketUpdate
     * The market_update
     */
    public void setMarketUpdate(String marketUpdate) {
        this.marketUpdate = marketUpdate;
    }

    /**
     *
     * @return
     * The screenshots
     */
    public List<String> getScreenshots() {
        return screenshots;
    }

    /**
     *
     * @param screenshots
     * The screenshots
     */
    public void setScreenshots(List<String> screenshots) {
        this.screenshots = screenshots;
    }

    /**
     *
     * @return
     * The created
     */
    public String getCreated() {
        return created;
    }

    /**
     *
     * @param created
     * The created
     */
    public void setCreated(String created) {
        this.created = created;
    }

    /**
     *
     * @return
     * The lang
     */
    public String getLang() {
        return lang;
    }

    /**
     *
     * @param lang
     * The lang
     */
    public void setLang(String lang) {
        this.lang = lang;
    }

    /**
     *
     * @return
     * The fromDeveloper
     */
    public List<Object> getFromDeveloper() {
        return fromDeveloper;
    }

    /**
     *
     * @param fromDeveloper
     * The from_developer
     */
    public void setFromDeveloper(List<Object> fromDeveloper) {
        this.fromDeveloper = fromDeveloper;
    }

    /**
     *
     * @return
     * The i18nLang
     */
    public List<String> getI18nLang() {
        return i18nLang;
    }

    /**
     *
     * @param i18nLang
     * The i18n_lang
     */
    public void setI18nLang(List<String> i18nLang) {
        this.i18nLang = i18nLang;
    }

    /**
     *
     * @return
     * The priceI18nCountries
     */
    public List<Object> getPriceI18nCountries() {
        return priceI18nCountries;
    }

    /**
     *
     * @param priceI18nCountries
     * The price_i18n_countries
     */
    public void setPriceI18nCountries(List<Object> priceI18nCountries) {
        this.priceI18nCountries = priceI18nCountries;
    }

    /**
     *
     * @return
     * The shortDesc
     */
    public String getShortDesc() {
        return shortDesc;
    }

    /**
     *
     * @param shortDesc
     * The short_desc
     */
    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    /**
     *
     * @return
     * The downloads
     */
    public String getDownloads() {
        return downloads;
    }

    /**
     *
     * @param downloads
     * The downloads
     */
    public void setDownloads(String downloads) {
        this.downloads = downloads;
    }

    /**
     *
     * @return
     * The downloadsMin
     */
    public Integer getDownloadsMin() {
        return downloadsMin;
    }

    /**
     *
     * @param downloadsMin
     * The downloads_min
     */
    public void setDownloadsMin(Integer downloadsMin) {
        this.downloadsMin = downloadsMin;
    }

    /**
     *
     * @return
     * The downloadsMax
     */
    public Integer getDownloadsMax() {
        return downloadsMax;
    }

    /**
     *
     * @param downloadsMax
     * The downloads_max
     */
    public void setDownloadsMax(Integer downloadsMax) {
        this.downloadsMax = downloadsMax;
    }

    /**
     *
     * @return
     * The developer
     */
    public String getDeveloper() {
        return developer;
    }

    /**
     *
     * @param developer
     * The developer
     */
    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    /**
     *
     * @return
     * The numberRatings
     */
    public Integer getNumberRatings() {
        return numberRatings;
    }

    /**
     *
     * @param numberRatings
     * The number_ratings
     */
    public void setNumberRatings(Integer numberRatings) {
        this.numberRatings = numberRatings;
    }

    /**
     *
     * @return
     * The icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     *
     * @param icon
     * The icon
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     *
     * @return
     * The icon72
     */
    public String getIcon72() {
        return icon72;
    }

    /**
     *
     * @param icon72
     * The icon_72
     */
    public void setIcon72(String icon72) {
        this.icon72 = icon72;
    }

    /**
     *
     * @return
     * The website
     */
    public String getWebsite() {
        return website;
    }

    /**
     *
     * @param website
     * The website
     */
    public void setWebsite(String website) {
        this.website = website;
    }

    /**
     *
     * @return
     * The promoVideo
     */
    public String getPromoVideo() {
        return promoVideo;
    }

    /**
     *
     * @param promoVideo
     * The promo_video
     */
    public void setPromoVideo(String promoVideo) {
        this.promoVideo = promoVideo;
    }

    /**
     *
     * @return
     * The marketUrl
     */
    public String getMarketUrl() {
        return marketUrl;
    }

    /**
     *
     * @param marketUrl
     * The market_url
     */
    public void setMarketUrl(String marketUrl) {
        this.marketUrl = marketUrl;
    }

    /**
     *
     * @return
     * The deepLink
     */
    public String getDeepLink() {
        return deepLink;
    }

    /**
     *
     * @param deepLink
     * The deep_link
     */
    public void setDeepLink(String deepLink) {
        this.deepLink = deepLink;
    }

}
