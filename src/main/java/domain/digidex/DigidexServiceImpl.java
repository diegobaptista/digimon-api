package domain.digidex;

import config.error.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import presentation.digidex.DigidexDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DigidexServiceImpl implements DigidexService {

    private DigidexRepository digidexRepository;
    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public DigidexServiceImpl(DigidexRepository digidexRepository) {
        this.digidexRepository = digidexRepository;
    }

    @Override
    public List<DigidexDTO> list() {
        return digidexRepository.findAll().stream().map(digidex -> {
            return modelMapper.map(digidex, DigidexDTO.class);
        }).collect(Collectors.toList());
    }

    @Override
    public DigidexDTO getById(Integer id) {
        validateEntityExistsById(id);
        return modelMapper.map(digidexRepository.findById(id).get(), DigidexDTO.class);
    }

    @Override
    public DigidexDTO getByName(String name) {
        validateEntityExistsByName(name);
        return modelMapper.map(digidexRepository.findByName(name).stream().findFirst().get(), DigidexDTO.class);
    }

    @Override
    public DigidexDTO save(DigidexDTO digidexDTO) {
        return modelMapper.map(
                digidexRepository.save(modelMapper.map(digidexDTO, Digidex.class)),
                DigidexDTO.class);
    }

    @Override
    public DigidexDTO update(DigidexDTO digidexDTO) {
        validateEntityExistsById(digidexDTO.getId());
        return modelMapper.map(
                digidexRepository.save(modelMapper.map(digidexDTO, Digidex.class)),
                DigidexDTO.class);
    }

    @Override
    public void deleteById(Integer id) {
        validateEntityExistsById(id);
        digidexRepository.deleteById(id);
    }

    private void validateEntityExistsByName(String name) {
        if(digidexRepository.findByName(name).isEmpty()) {
            throw new NotFoundException();
        }
    }

    private void validateEntityExistsById(Integer id) {
        if (digidexRepository.findById(id).isEmpty()) {
            throw new NotFoundException();
        }
    }

}
