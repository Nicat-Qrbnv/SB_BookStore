package org.ltclab.sb_bookstore.client;

import org.ltclab.sb_bookstore.dto.responseDTO.BookResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "GoodReadsAPI", url = "https://goodreads-books.p.rapidapi.com")
public interface GoodReadsClient {

    String API_HOST_HEADER_NAME = "X-RapidAPI-Host";
    String API_KEY_HEADER_NAME = "X-RapidAPI-Key";
    @GetMapping(value = "/search")
    List<BookResponseDTO> searchBookInGoodReads(@RequestHeader(name = API_HOST_HEADER_NAME) String apiHostHeader,
                                                @RequestHeader(name = API_KEY_HEADER_NAME) String apiKeyHeader,
                                                @RequestParam String q);


}
