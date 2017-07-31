package com.tjj.service;

import com.google.inject.Inject;
import com.tjj.model.pkg.PackageBody;
import com.tjj.repository.PackageBodyRepository;
import org.springframework.transaction.annotation.Transactional;

public class PackageBodyService {

  private PackageBodyRepository packageBodyRepository;

  @Inject public PackageBodyService(PackageBodyRepository packageBodyRepository) {
    this.packageBodyRepository = packageBodyRepository;
  }

  @Transactional
  public void add(PackageBody packageBody) {
    packageBodyRepository.save(packageBody);
  }

  @Transactional
  public void del(PackageBody packageBody) {
    packageBodyRepository.delete(packageBody);
  }

  @Transactional
  public void delById(Long id) {
    packageBodyRepository.delete(id);
  }

  public PackageBody get(long id) {
    return packageBodyRepository.findOne(id);
  }
}
