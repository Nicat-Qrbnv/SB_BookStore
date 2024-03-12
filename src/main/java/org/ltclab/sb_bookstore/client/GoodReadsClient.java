package org.ltclab.sb_bookstore.client;

import org.ltclab.sb_bookstore.dto.responseDTO.BookResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "GoodReadsAPI", url = "https://goodreads-books.p.rapidapi.com")
public interface GoodReadsClient {
    @GetMapping(value = "/search")
    List<BookResponseDTO> searchBookInGoodReads(@RequestHeader(name = "X-RapidAPI-Host") String apiHostHeader,
                                                @RequestHeader(name = "X-RapidAPI-Key") String apiKeyHeader,
                                                @RequestParam String q);


}
