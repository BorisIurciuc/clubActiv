package gr36;

import java.util.Random;

public class Images {

  private final String[] imageLinks = {
      "https://my-activity-images.s3.eu-north-1.amazonaws.com/1.webp",
      "https://my-activity-images.s3.eu-north-1.amazonaws.com/2.webp",
      "https://my-activity-images.s3.eu-north-1.amazonaws.com/3.webp",
      "https://my-activity-images.s3.eu-north-1.amazonaws.com/4.webp",
      "https://my-activity-images.s3.eu-north-1.amazonaws.com/5.webp",
      "https://my-activity-images.s3.eu-north-1.amazonaws.com/6.webp",
      "https://my-activity-images.s3.eu-north-1.amazonaws.com/7.webp",
      "https://my-activity-images.s3.eu-north-1.amazonaws.com/8.webp",
      "https://my-activity-images.s3.eu-north-1.amazonaws.com/9.webp",
      "https://my-activity-images.s3.eu-north-1.amazonaws.com/10.webp"
  };


  public String getRandomImage() {
    Random random = new Random();
    int index = random.nextInt(imageLinks.length);
    return imageLinks[index];
  }
}
