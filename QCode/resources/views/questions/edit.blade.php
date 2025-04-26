@extends('layouts.app')

@section('title', 'Edit Question')

@section('additional-styles')
.form-container {
    background: white;
    padding: 2rem;
    border-radius: 8px;
    box-shadow: 0 1px 3px rgba(0,0,0,0.1);
    margin-bottom: 2rem;
}
.tag-checkbox {
    display: inline-block;
    margin-right: 1em;
    margin-bottom: 0.5em;
    padding: 0.5em 1em;
    background: #f8f9fa;
    border-radius: 4px;
    transition: background-color 0.2s;
}
.tag-checkbox:hover {
    background: #e9ecef;
}
.preview {
    margin-top: 1em;
    padding: 1em;
    border: 1px solid #dee2e6;
    border-radius: 4px;
    background: #f8f9fa;
}
.code-preview {
    font-family: monospace;
    background: #f8f9fa;
    padding: 1rem;
    border-radius: 4px;
    margin-top: 0.5rem;
}
@endsection

@section('content')
<div class="container">
    <div class="mb-4">
        <a href="{{ route('questions.show', $question) }}" class="text-decoration-none">
            <i class="bi bi-arrow-left"></i> Back to Question
        </a>
    </div>

    <div class="form-container">
        <h1 class="mb-4">Edit Question</h1>

        @if($errors->any())
            <div class="alert alert-danger">
                <ul class="mb-0">
                    @foreach($errors->all() as $error)
                        <li>{{ $error }}</li>
                    @endforeach
                </ul>
            </div>
        @endif

        <form method="POST" action="{{ route('questions.update', $question) }}">
            @csrf
            @method('PUT')
            
            <div class="form-group mb-4">
                <label for="title" class="form-label">Title</label>
                <input type="text" id="title" name="title" 
                    class="form-control @error('title') is-invalid @enderror"
                    value="{{ old('title', $question->title) }}" 
                    placeholder="Be specific and imagine you're asking a question to another person"
                    required>
                @error('title')
                    <div class="invalid-feedback">{{ $message }}</div>
                @enderror
            </div>

            <div class="form-group mb-4">
                <label for="body" class="form-label">Body</label>
                <textarea id="body" name="body" rows="10" 
                    class="form-control @error('body') is-invalid @enderror"
                    placeholder="Include all the information someone would need to answer your question"
                    required>{{ old('body', $question->body) }}</textarea>
                @error('body')
                    <div class="invalid-feedback">{{ $message }}</div>
                @enderror
            </div>

            <div class="form-group mb-4">
                <label for="code_snippet" class="form-label">Code (optional)</label>
                <textarea id="code_snippet" name="code_snippet" rows="6"
                    class="form-control font-monospace"
                    placeholder="Share your code if relevant to the question">{{ old('code_snippet', $question->code_snippet) }}</textarea>
                <div id="code-preview" class="preview" style="display: none;">
                    <pre><code class="language-php"></code></pre>
                </div>
            </div>

            <div class="form-group mb-4">
                <label class="form-label d-block">Tags</label>
                <div class="tag-list">
                    @foreach($tags as $tag)
                        <label class="tag-checkbox">
                            <input type="checkbox" name="tags[]" value="{{ $tag->id }}"
                                {{ in_array($tag->id, old('tags', $question->tags->pluck('id')->toArray())) ? 'checked' : '' }}>
                            {{ $tag->name }}
                        </label>
                    @endforeach
                </div>
                @error('tags')
                    <div class="invalid-feedback d-block">{{ $message }}</div>
                @enderror
            </div>

            <div class="form-group">
                <button type="submit" class="btn btn-primary">Update Question</button>
                <a href="{{ route('questions.show', $question) }}" class="btn btn-outline-secondary">Cancel</a>
            </div>
        </form>
    </div>
</div>
@endsection

@section('scripts')
<script>
// Live preview for code snippet
const codeInput = document.getElementById('code_snippet');
const preview = document.getElementById('code-preview');
const code = preview.querySelector('code');

if (codeInput.value) {
    code.textContent = codeInput.value;
    preview.style.display = 'block';
    Prism.highlightElement(code);
}

codeInput.addEventListener('input', function() {
    if (this.value) {
        code.textContent = this.value;
        preview.style.display = 'block';
        Prism.highlightElement(code);
    } else {
        preview.style.display = 'none';
    }
});
</script>
@endsection