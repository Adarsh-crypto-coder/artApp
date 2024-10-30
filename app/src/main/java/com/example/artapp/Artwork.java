package com.example.artapp;

public class Artwork {
    private String id;
    private String title;
    private String artistDisplay;
    private String dateDisplay;
    private String mediumDisplay;
    private String dimensions;
    private String creditLine;
    private String departmentTitle;
    private String galleryTitle;
    private String galleryId;
    private String imageId;
    private String placeOfOrigin;
    private String artworkTypeTitle;
    private String artistName;
    private String artistDetails;


    private static final String BASE_URL = "https://www.artic.edu/iiif/2/";
    private static final String THUMBNAIL_SPEC = "/full/200,/0/default.jpg";
    private static final String FULL_IMAGE_SPEC = "/full/843,/0/default.jpg";

    public Artwork(String id, String imageId, String galleryId, String galleryTitle, String departmentTitle,
                   String creditLine, String dimensions, String mediumDisplay, String dateDisplay, String artistDisplay,
                   String title, String placeOfOrigin, String artworkTypeTitle) {
        this.id = id;
        this.imageId = imageId;
        this.galleryId = galleryId;
        this.galleryTitle = galleryTitle;
        this.departmentTitle = departmentTitle;
        this.creditLine = creditLine;
        this.dimensions = dimensions;
        this.mediumDisplay = mediumDisplay;
        this.dateDisplay = dateDisplay;
        this.artistDisplay = artistDisplay;
        this.title = title;
        this.placeOfOrigin = placeOfOrigin;
        this.artworkTypeTitle = artworkTypeTitle;

        if (artistDisplay != null && artistDisplay.contains("\n")) {
            String[] parts = artistDisplay.split("\n");
            this.artistName = parts[0];
            this.artistDetails = parts[1];
        } else {
            this.artistName = artistDisplay;
            this.artistDetails = "";
        }
    }

    public String getId() {
        return id;
    }

    public String getArtworkTypeTitle() {
        return artworkTypeTitle;
    }

    public String getPlaceOfOrigin() {
        return placeOfOrigin;
    }

    public String getThumbnailUrl() {
        return BASE_URL + imageId + THUMBNAIL_SPEC;
    }

    public String getFullImageUrl() {
        return BASE_URL + imageId + FULL_IMAGE_SPEC;
    }

    public String getDateDisplay() {
        return dateDisplay;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getArtistDetails() {
        return artistDetails;
    }

    public String getArtistDisplay() {
        if (!artistDetails.isEmpty()) {
            return artistName + "\n" + artistDetails;
        }
        return artistName;
    }

    public String getCombinedTypeAndMedium() {
        return artworkTypeTitle + " - " + mediumDisplay;
    }

    public String getTitle() {
        return title;
    }

    public String getMediumDisplay() {
        return mediumDisplay;
    }

    public String getDimensions() {
        return dimensions;
    }

    public String getCreditLine() {
        return creditLine;
    }

    public String getDepartmentTitle() {
        return departmentTitle;
    }

    public String getGalleryTitle() {
        return galleryTitle;
    }

    public String getGalleryId() {
        return galleryId;
    }

    public String getImageId() {
        return imageId;
    }
}






