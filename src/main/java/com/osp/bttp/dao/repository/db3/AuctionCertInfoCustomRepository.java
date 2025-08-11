package com.osp.bttp.dao.repository.db3;

import com.osp.bttp.dao.model.dto.db3.AuctioneerWithCertDto;

import java.util.List;

public interface AuctionCertInfoCustomRepository {

    List<AuctioneerWithCertDto> getAllCert();
}
