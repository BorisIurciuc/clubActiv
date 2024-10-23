package gr36.clubActiv.services.interfaces;

import gr36.clubActiv.domain.entity.Response;
import java.util.List;

public interface ResponseService {


  Response addResponse(Long reviewId, Response response);

  void deleteResponse(Long responseId);

  List<Response> getResponsesByReviewId(Long reviewId);
}
