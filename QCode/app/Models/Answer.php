<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;
use Illuminate\Database\Eloquent\Relations\MorphMany;

class Answer extends Model
{
    use HasFactory;

    protected $fillable = [
        'body',
        'code_snippet',
        'user_id',
        'question_id'
    ];

    public function user(): BelongsTo
    {
        return $this->belongsTo(User::class);
    }

    public function question(): BelongsTo
    {
        return $this->belongsTo(Question::class);
    }

    public function votes(): MorphMany
    {
        return $this->morphMany(Vote::class, 'voteable');
    }

    public function isSolution()
    {
        return $this->question->solution_id === $this->id;
    }

    public function hasBeenVotedByUser($userId): bool
    {
        return $this->votes()->where('user_id', $userId)->exists();
    }

    public function getTotalVotesAttribute(): int
    {
        return $this->votes->sum('value');
    }
}
