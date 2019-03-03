package ru.ai.pilgrim.point;
import android.widget.ImageView;

import java.util.Date;

 public class pointsdata {
        private String description;
        private Double latitude,longitude;
        private Date pointDate;
        private ImageView photo;
        private int id;

        public pointsdata(String description, Double latitude,Double longitude,Date pointDate,
                     ImageView photo, int id) {
            this.description=description;
            this.latitude=latitude;
            this.longitude=longitude;
            this.pointDate=pointDate;
            this.photo=photo;
            this.id=id;
        }

        public String getDescription() {
            return description;
        }
        public void setDescription(String description) {
            this.description = description;
        }

        public Double getLatitude() {
            return latitude;
        }
        public void setLatitude (Double latitude) {
         this.latitude = latitude;
        }

        public Double getLongitude() {
         return longitude;
        }
        public void setLongitude (Double longitude) {
            this.longitude = longitude;
        }

        public Date getPointDate() {
            return pointDate;
        }
        public void setPointDate (Date pointDate) {
            this.pointDate = pointDate;
        }
        public ImageView getPhoto(ImageView photo) {
            return photo;
        }
        public void setPhoto(ImageView photo) {
            this.photo = photo;
        }

        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
    }
}
