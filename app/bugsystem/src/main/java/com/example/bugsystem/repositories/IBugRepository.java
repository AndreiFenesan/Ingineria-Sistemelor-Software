package com.example.bugsystem.repositories;

import com.example.bugsystem.model.Bug;
import com.example.bugsystem.model.Programmer;
import com.example.bugsystem.model.Status;
import com.example.bugsystem.model.Tester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBugRepository extends JpaRepository<Bug, Integer> {
    List<Bug> findBugsByStatusIs(Status status);

    List<Bug> findBugsByStatusIsAndBugSolverIs(Status status, Programmer bugSolver);

    int deleteBugByIdAndBugPoster(Integer bugId,Tester bugPoster);
}
