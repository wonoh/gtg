package kr.ac.gachon.gtg.service;

import kr.ac.gachon.gtg.domain.Major;
import kr.ac.gachon.gtg.persistence.MajorRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service("MajorService")
@Log
public class MajorServiceImpl implements MajorService {
    @Autowired
    MajorRepository majorRepo;

    @Override
    public ArrayList<Major> findAll() {
        ArrayList<Major> list = new ArrayList<>();

        majorRepo.findAll()
                .forEach(list::add);

        return list;
    }
}
