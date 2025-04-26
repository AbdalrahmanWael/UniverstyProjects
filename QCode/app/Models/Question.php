<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;
use Illuminate\Database\Eloquent\Relations\HasMany;
use Illuminate\Database\Eloquent\Relations\MorphMany;
use Illuminate\Database\Eloquent\Relations\BelongsToMany;

class Question extends Model
{
    use HasFactory;

    protected $fillable = [
        'title',
        'body',
        'code_snippet',
        'user_id',
        'solution_id',
        'view_count'
    ];

    protected $with = ['tags', 'user'];

    protected $casts = [
        'view_count' => 'integer'
    ];

    public function user(): BelongsTo
    {
        return $this->belongsTo(User::class);
    }

    public function answers(): HasMany
    {
        return $this->hasMany(Answer::class);
    }

    public function tags(): BelongsToMany
    {
        return $this->belongsToMany(Tag::class, 'question_tags');
    }

    public function votes(): MorphMany
    {
        return $this->morphMany(Vote::class, 'voteable');
    }

    public function solution(): BelongsTo
    {
        return $this->belongsTo(Answer::class, 'solution_id');
    }

    public function incrementViewCount(): void
    {
        $this->timestamps = false;
        $this->increment('view_count');
        $this->timestamps = true;
    }

    public function getTotalVotesAttribute(): int
    {
        return $this->votes->sum('value');
    }

    public function hasBeenVotedByUser($userId): bool
    {
        return $this->votes()->where('user_id', $userId)->exists();
    }
}
