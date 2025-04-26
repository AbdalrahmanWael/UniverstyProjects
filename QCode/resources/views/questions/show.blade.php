@extends('layouts.app')

@section('title', $question->title)

@section('additional-styles')
.content-container {
    background: white;
    border: 1px solid #e1e4e8;
    border-radius: 8px;
    padding: 2em;
    margin-bottom: 1em;
    box-shadow: 0 1px 3px rgba(0,0,0,0.1);
}

.vote-buttons {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-right: 1rem;
}

.vote-button {
    border: none;
    background: none;
    font-size: 0.9rem;
    color: #586069;
    cursor: pointer;
    padding: 0.5rem 1rem;
    transition: all 0.2s ease;
    border-radius: 8px;
    min-width: 80px;
    height: 40px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-weight: 500;
}

.vote-button:hover:not(:disabled) {
    transform: translateY(-2px);
    background-color: #f8f9fa;
}

.vote-button[value="up"]:hover:not(:disabled) {
    color: #2ea44f;
    background-color: #e1f3e6;
}

.vote-button[value="down"]:hover:not(:disabled) {
    color: #d73a49;
    background-color: #ffdce0;
}

.vote-button.active[value="up"] {
    color: #2ea44f;
    background-color: #e1f3e6;
}

.vote-button.active[value="down"] {
    color: #d73a49;
    background-color: #ffdce0;
}

.vote-button:disabled {
    opacity: 0.6;
    cursor: not-allowed;
    transform: none;
}

.vote-count {
    font-size: 1.25rem;
    font-weight: bold;
    margin: 0.75rem 0;
    color: #444;
    min-width: 30px;
    text-align: center;
}

.vote-count.flash {
    animation: vote-flash 0.6s cubic-bezier(0.175, 0.885, 0.32, 1.275);
}

@keyframes vote-flash {
    0%, 100% { transform: scale(1); }
    50% { transform: scale(1.3); }
}

.solution-badge {
    background: #2ea44f;
    color: white;
    padding: 0.4em 0.8em;
    border-radius: 3px;
    display: inline-block;
    margin-bottom: 1em;
}

.tag {
    display: inline-block;
    padding: 0.4em 0.8em;
    background: #e1ecf4;
    color: #39739d;
    border-radius: 3px;
    margin: 0.3em;
    text-decoration: none;
}

.tag:hover {
    background: #cde0ef;
    color: #2c5777;
    text-decoration: none;
}

.code-block {
    background: #f6f8fa;
    padding: 1.5rem;
    border-radius: 6px;
    border: 1px solid #e1e4e8;
    margin: 1.5rem 0;
    position: relative;
}

.code-block pre {
    margin: 0;
    padding: 0;
    background: transparent;
}

.code-block code {
    font-family: 'SFMono-Regular', Consolas, 'Liberation Mono', Menlo, monospace;
    font-size: 0.9rem;
    line-height: 1.5;
    white-space: pre;
    word-break: normal;
    word-wrap: normal;
    tab-size: 4;
    hyphens: none;
    overflow-x: auto;
    display: block;
}

.user-info {
    display: flex;
    align-items: center;
    gap: 0.5em;
    margin-top: 1em;
    padding: 1em;
    background: #f6f8fa;
    border-radius: 6px;
}

.user-avatar {
    width: 32px;
    height: 32px;
    border-radius: 50%;
    background: #e1e4e8;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #586069;
}

.question-actions {
    display: flex;
    gap: 1em;
    margin-top: 1em;
    padding-top: 1em;
    border-top: 1px solid #e1e4e8;
}

.action-buttons {
    display: flex;
    gap: 0.5rem;
    margin-left: auto;
}

.answer {
    background: white;
    border: 1px solid #e1e4e8;
    border-radius: 8px;
    padding: 2em;
    margin-bottom: 2em;
    box-shadow: 0 1px 3px rgba(0,0,0,0.1);
    transition: border-color 0.2s ease;
}

.answer.border-success {
    border: 2px solid #2ea44f;
}

.answers-section {
    margin-top: 3rem;
    padding-top: 2rem;
    border-top: 2px solid #e1e4e8;
}

.answers-section-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 2rem;
}

.answer-form-section {
    background: white;
    border: 1px solid #e1e4e8;
    border-radius: 8px;
    padding: 2em;
    margin-top: 3rem;
    box-shadow: 0 1px 3px rgba(0,0,0,0.1);
}

.answer-form-section h3 {
    color: #24292e;
    margin-bottom: 1.5rem;
    font-size: 1.5rem;
}

.answer-form-section .form-label {
    font-weight: 500;
    color: #24292e;
    margin-bottom: 0.5rem;
}

.answer-form-section textarea {
    border-color: #e1e4e8;
    transition: border-color 0.15s ease-in-out;
}

.answer-form-section textarea:focus {
    border-color: #0366d6;
    box-shadow: 0 0 0 3px rgba(3, 102, 214, 0.1);
}

.answer-form-section .btn-primary {
    margin-top: 1rem;
    padding: 0.5rem 1.5rem;
}
@endsection

@section('content')
<div class="container">
    <div class="mb-4">
        <a href="{{ route('questions.index') }}" class="text-decoration-none">
            <i class="bi bi-arrow-left"></i> Back to Questions
        </a>
    </div>

    <div class="question-content">
        <h1 class="mb-3">{{ $question->title }}</h1>

        <div class="tags mb-4">
            @foreach($question->tags as $tag)
                <a href="{{ route('questions.index', ['tag' => $tag->name]) }}" class="tag">
                    {{ $tag->name }}
                </a>
            @endforeach
        </div>

        <div class="d-flex gap-4">
            <div class="votes text-center">
                @auth
                    @can('vote', $question)
                        <form action="{{ route('questions.vote', $question) }}" method="POST" class="vote-form" data-question-id="{{ $question->id }}">
                            @csrf
                            <button type="submit"  name="vote" value="up" class="vote-button m-auto {{ $question->userVote?->value === 1 ? 'active' : '' }}">
                                Upvote
                            </button>
                            <div class="fs-5 fw-bold vote-count" id="vote-count-{{ $question->id }}">{{ $question->votes()->sum('value') }}</div>
                            <button type="submit" name="vote" value="down" class="vote-button {{ $question->userVote?->value === -1 ? 'active' : '' }}">
                                Downvote
                            </button>
                        </form>
                    @else
                        <form class="vote-form">
                            <button type="button" class="vote-button m-auto" disabled title="You cannot vote on your own question">
                                Upvote
                            </button>
                            <div class="fs-5 fw-bold vote-count">{{ $question->votes()->sum('value') }}</div>
                            <button type="button" class="vote-button" disabled title="You cannot vote on your own question">
                                Downvote
                            </button>
                        </form>
                    @endcan
                @else
                    <form class="vote-form">
                        <button type="button" class="vote-button m-auto" disabled title="Login to vote">
                            Upvote
                        </button>
                        <div class="fs-5 fw-bold vote-count">{{ $question->votes()->sum('value') }}</div>
                        <button type="button" class="vote-button" disabled title="Login to vote">
                            Downvote
                        </button>
                    </form>
                @endauth
            </div>

            <div class="flex-grow-1">
                <div class="question-body">
                    {!! nl2br(e($question->body)) !!}
                </div>

                @if($question->code_snippet)
                    <div class="code-block">
                        <pre><code class="language-php">{{ $question->code_snippet }}</code></pre>
                    </div>
                @endif

                <div class="user-info">
                    <div class="user-avatar">
                        {{ strtoupper(substr($question->user->name, 0, 1)) }}
                    </div>
                    <div>
                        <div>
                            <a href="{{ route('profile.show', $question->user) }}" class="text-decoration-none">
                                {{ $question->user->name }}
                            </a>
                        </div>
                        <div class="text-muted small">
                            asked {{ $question->created_at->diffForHumans() }}
                        </div>
                    </div>
                </div>

                @can('update', $question)
                    <div class="question-actions">
                        <a href="{{ route('questions.edit', $question) }}" class="btn btn-outline-primary">
                            <i class="bi bi-pencil"></i> Edit
                        </a>
                        <form action="{{ route('questions.destroy', $question) }}" method="POST" class="d-inline" onsubmit="return confirm('Are you sure you want to delete this question?');">
                            @csrf
                            @method('DELETE')
                            <button type="submit" class="btn btn-outline-danger">
                                <i class="bi bi-trash"></i> Delete
                            </button>
                        </form>
                    </div>
                @endcan
            </div>
        </div>
    </div>

    <div class="answers-section">
        <div class="answers-section-header">
            <h2 class="mb-0">{{ $question->answers->count() }} {{ Str::plural('Answer', $question->answers->count()) }}</h2>
            @auth
                <a href="#answer-form" class="btn btn-outline-primary">Write an Answer</a>
            @endauth
        </div>

        @foreach($question->answers as $answer)
            <div class="answer {{ $question->solution_id === $answer->id ? 'border-success' : '' }}">
                @if($question->solution_id === $answer->id)
                    <div class="solution-badge">
                        <i class="bi bi-check-circle"></i> Solution
                    </div>
                @endif

                <div class="d-flex gap-4">
                    <div class="votes text-center">
                        @auth
                            @can('vote', $answer)
                                <form action="{{ route('answers.vote', $answer) }}" method="POST" class="vote-form" data-answer-id="{{ $answer->id }}">
                                    @csrf
                                    <button type="submit" name="vote" value="up" class="vote-button m-auto {{ $answer->userVote?->value === 1 ? 'active' : '' }}">
                                        Upvote
                                    </button>
                                    <div class="fs-5 fw-bold vote-count" id="vote-count-answer-{{ $answer->id }}">{{ $answer->votes()->sum('value') }}</div>
                                    <button type="submit" name="vote" value="down" class="vote-button {{ $answer->userVote?->value === -1 ? 'active' : '' }}">
                                        Downvote
                                    </button>
                                </form>
                            @else
                                <form class="vote-form">
                                    <button type="button" class="vote-button m-auto " disabled title="You cannot vote on your own answer">
                                        Upvote
                                    </button>
                                    <div class="fs-5 fw-bold vote-count">{{ $answer->votes()->sum('value') }}</div>
                                    <button type="button" class="vote-button" disabled title="You cannot vote on your own answer">
                                        Downvote
                                    </button>
                                </form>
                            @endcan
                        @else
                            <form class="vote-form">
                                <button type="button" class="vote-button m-auto " disabled title="Login to vote">
                                    Upvote
                                </button>
                                <div class="fs-5 fw-bold vote-count">{{ $answer->votes()->sum('value') }}</div>
                                <button type="button" class="vote-button" disabled title="Login to vote">
                                    Downvote
                                </button>
                            </form>
                        @endauth
                    </div>

                    <div class="flex-grow-1">
                        <div class="answer-body">
                            {!! nl2br(e($answer->body)) !!}
                        </div>

                        @if($answer->code_snippet)
                            <div class="code-block">
                                <pre><code class="language-php">{{ $answer->code_snippet }}</code></pre>
                            </div>
                        @endif

                        <div class="user-info">
                            <div class="user-avatar">
                                {{ strtoupper(substr($answer->user->name, 0, 1)) }}
                            </div>
                            <div>
                                <div>
                                    <a href="{{ route('profile.show', $answer->user) }}" class="text-decoration-none">
                                        {{ $answer->user->name }}
                                    </a>
                                </div>
                                <div class="text-muted small">
                                    answered {{ $answer->created_at->diffForHumans() }}
                                </div>
                            </div>
                        </div>

                        <div class="d-flex gap-3 mt-3">
                            @can('update', $answer)
                                <a href="{{ route('answers.edit', $answer) }}" class="btn btn-outline-primary btn-sm">
                                    <i class="bi bi-pencil"></i> Edit
                                </a>
                                <form action="{{ route('answers.destroy', $answer) }}" method="POST" class="d-inline" onsubmit="return confirm('Are you sure you want to delete this answer?');">
                                    @csrf
                                    @method('DELETE')
                                    <button type="submit" class="btn btn-outline-danger btn-sm">
                                        <i class="bi bi-trash"></i> Delete
                                    </button>
                                </form>
                            @endcan

                            @can('markAsSolution', [$question, $answer])
                                <form action="{{ route('answers.solution', $answer) }}" method="POST" class="d-inline">
                                    @csrf
                                    <button type="submit" class="btn btn-outline-success btn-sm">
                                        <i class="bi bi-check-circle"></i> Mark as Solution
                                    </button>
                                </form>
                            @endcan
                        </div>
                    </div>
                </div>
            </div>
        @endforeach
    </div>

    @auth
        <div class="answer-form-section" id="answer-form">
            <h3>Your Answer</h3>
            <form action="{{ route('answers.store', $question) }}" method="POST">
                @csrf
                <div class="mb-4">
                    <label for="body" class="form-label">Answer</label>
                    <textarea class="form-control" id="body" name="body" rows="6" 
                        required placeholder="Write your answer here...">{{ old('body') }}</textarea>
                </div>
                <div class="mb-4">
                    <label for="code_snippet" class="form-label">Code Snippet (optional)</label>
                    <textarea class="form-control font-monospace" id="code_snippet" name="code_snippet" 
                        rows="6" placeholder="Share your code if it helps explain your answer">{{ old('code_snippet') }}</textarea>
                </div>
                <button type="submit" class="btn btn-primary">Post Your Answer</button>
            </form>
        </div>
    @else
        <div class="answer-form-section text-center py-5">
            <h3>Your Answer</h3>
            <p class="text-muted mb-4">Help the community by sharing your knowledge</p>
            <a href="{{ route('login') }}" class="btn btn-primary btn-lg">Log in to Answer</a>
        </div>
    @endauth
</div>
@endsection

@section('scripts')
<script>
document.addEventListener('DOMContentLoaded', function() {
    // Initialize syntax highlighting for all existing code blocks
    document.querySelectorAll('pre code').forEach((block) => {
        if (!block.className.includes('language-')) {
            block.className += ' language-php';
        }
        Prism.highlightElement(block);
    });

    // Live preview for new answer code snippet
    const codeInput = document.getElementById('code_snippet');
    const preview = document.createElement('div');
    preview.id = 'code-preview';
    preview.className = 'code-block mt-3';
    preview.style.display = 'none';
    preview.innerHTML = '<pre><code class="language-php"></code></pre>';
    
    if (codeInput) {
        codeInput.parentNode.insertBefore(preview, codeInput.nextSibling);
        const code = preview.querySelector('code');

        function updatePreview() {
            if (codeInput.value.trim()) {
                code.textContent = codeInput.value;
                preview.style.display = 'block';
                Prism.highlightElement(code);
            } else {
                preview.style.display = 'none';
            }
        }

        // Initialize preview if there's existing content
        updatePreview();

        // Update preview on input
        codeInput.addEventListener('input', updatePreview);
    }

    // Voting functionality
    const voteForms = document.querySelectorAll('.vote-form');
    
    voteForms.forEach(form => {
        const questionId = form.dataset.questionId;
        const answerId = form.dataset.answerId;
        if (!questionId && !answerId) return; // Skip if not a voting form
        
        form.addEventListener('submit', function(e) {
            e.preventDefault();
            const voteValue = e.submitter.value;
            const voteCount = document.getElementById(answerId ? 
                `vote-count-answer-${answerId}` : 
                `vote-count-${questionId}`
            );
            const upButton = form.querySelector('button[value="up"]');
            const downButton = form.querySelector('button[value="down"]');
            
            // Disable buttons during request
            upButton.disabled = true;
            downButton.disabled = true;
            
            fetch(this.action, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-CSRF-TOKEN': document.querySelector('meta[name="csrf-token"]').content
                },
                body: JSON.stringify({ vote: voteValue })
            })
            .then(response => response.json())
            .then(data => {
                // Update vote count with animation
                voteCount.textContent = data.total_votes;
                voteCount.classList.add('flash');
                setTimeout(() => voteCount.classList.remove('flash'), 500);
                
                // Update button states
                upButton.classList.toggle('active', data.userVote === 1);
                downButton.classList.toggle('active', data.userVote === -1);
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Failed to process vote. Please try again.');
            })
            .finally(() => {
                // Re-enable buttons
                upButton.disabled = false;
                downButton.disabled = false;
            });
        });
    });
});
</script>
@endsection