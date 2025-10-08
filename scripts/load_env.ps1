$envFile = ".\.env"

if (Test-Path $envFile) {
    Write-Host "Loading environment variables from $envFile..."

    foreach ($line in (Get-Content $envFile)) {

        if (-not [string]::IsNullOrWhiteSpace($line) -and -not $line.TrimStart().StartsWith("#")) {

            if ($line -match '^([\w.-]+)\s*=\s*(["'']?)(.*)\2$') {
                $key = $Matches[1]
                $value = $Matches[3]

                # Set the environment variable for the current PowerShell process
                [System.Environment]::SetEnvironmentVariable($key, $value, "Process")
                Write-Host "  -> Set $key"
            }
        }
    }
    Write-Host "Variables loaded."
} else {
    Write-Host "Warning: The file '$envFile' was not found. Make sure it exists in the project root."
}
