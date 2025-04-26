<?php

namespace App\Policies;

use App\Models\Answer;
use App\Models\Question;
use App\Models\User;
use Illuminate\Auth\Access\HandlesAuthorization;

class AnswerPolicy
{
    use HandlesAuthorization;

    public function update(User $user, Answer $answer)
    {
        return $user->id === $answer->user_id;
    }

    public function delete(User $user, Answer $answer)
    {
        return $user->id === $answer->user_id;
    }

    public function markSolution(User $user, Answer $answer, Question $question)
    {
        return $user->id === $question->user_id;
    }

    public function vote(User $user, Answer $answer)
    {
        return $user->id !== $answer->user_id;
    }
}