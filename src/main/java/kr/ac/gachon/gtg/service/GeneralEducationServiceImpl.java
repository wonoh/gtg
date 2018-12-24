package kr.ac.gachon.gtg.service;

import kr.ac.gachon.gtg.domain.GeneralEducation;
import kr.ac.gachon.gtg.persistence.GeneralEducationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service("GeneralEducationService")
public class GeneralEducationServiceImpl implements GeneralEducationService {
    @Autowired
    GeneralEducationRepository generalEducationRepo;

    @Override
    public ArrayList<GeneralEducation> findAll() {
        ArrayList<GeneralEducation> list = new ArrayList<>();

        generalEducationRepo.findAll()
                .forEach(list::add);

        return list;
    }
}
