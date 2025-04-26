<?php

namespace App\Policies;

use App\Models\Question;
use App\Models\User;
use Illuminate\Auth\Access\HandlesAuthorization;

class QuestionPolicy
{
    use HandlesAuthorization;

    public function update(User $user, Question $question)
    {
        return $user->id === $question->user_id;
    }

    public function delete(User $user, Question $question)
    {
        return $user->id === $question->user_id;
    }

    public function vote(User $user, Question $question)
    {
        return $user->id !== $question->user_id;
    }

    public function markSolution(User $user, Question $question)
    {
        return $user->id === $question->user_id;
    }
}