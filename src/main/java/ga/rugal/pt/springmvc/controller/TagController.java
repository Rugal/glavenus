package ga.rugal.pt.springmvc.controller;

import java.net.URI;

import ga.rugal.pt.core.entity.Tag;
import ga.rugal.pt.core.service.TagService;
import ga.rugal.pt.openapi.api.TagApi;
import ga.rugal.pt.openapi.model.NewTagDto;
import ga.rugal.pt.openapi.model.TagDto;
import ga.rugal.pt.springmvc.mapper.TagMapper;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * User controller.
 *
 * @author Rugal Bernstein
 */
@Api(tags = "TagController")
@RestController
@Slf4j
public class TagController implements TagApi {

  @Autowired
  private TagService tagService;

  @Override
  public ResponseEntity<TagDto> create(final @RequestBody NewTagDto newTagDto) {
    final Tag newTag = TagMapper.INSTANCE.to(newTagDto);
    final TagDto tagDto = TagMapper.INSTANCE.from(this.tagService.getDao().save(newTag));

    final URI location = ServletUriComponentsBuilder
            .fromCurrentRequest().path("/{id}")
            .buildAndExpand(tagDto.getTid()).toUri();

    return ResponseEntity
            .created(location)
            .body(tagDto);
  }
}
